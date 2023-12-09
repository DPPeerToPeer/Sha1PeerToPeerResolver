package com.example.network.internal.data.nodes.singleNodeConnection.repository

import com.example.common.IGetMyIdUseCase
import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.myPort.IMyPortRepository
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionFactory
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionHandler
import com.example.network.models.NodeMessage
import com.example.socketsFacade.IClientSocketFactory
import com.example.socketsFacade.IReadWriteSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SingleNodeConnectionRepository(
    private val scope: CoroutineScope,
    private val singleNodeConnectionFactory: ISingleNodeConnectionFactory,
    private val clientSocketFactory: IClientSocketFactory,
    private val myPortRepository: IMyPortRepository,
    private val getMyIdUseCase: IGetMyIdUseCase,
) : ISingleNodeConnectionRepository {

    private val sockets: MutableStateFlow<Map<NodeId, ISingleNodeConnectionHandler>> = MutableStateFlow(emptyMap())

    override suspend fun getOrCreateSingleConnectionHandlerAsClient(node: Node): ISingleNodeConnectionHandler =
        sockets.value[node.id] ?: createClientSocketAndRunHandlingMessages(node)

    override suspend fun createSingleConnectionHandlerAsServer(socket: IReadWriteSocket) {
        val singleConnectionsHandler = singleNodeConnectionFactory.create(
            socket = socket,
        )
        val nodeId = singleConnectionsHandler.listenNodeId()
        sockets.update { previousMap ->
            previousMap + (nodeId to singleConnectionsHandler)
        }

        singleConnectionsHandler.listenIncomingMessages()
    }

    override fun getIpOfSocket(nodeId: NodeId): String? =
        sockets.value[nodeId]?.socketIp

    private suspend fun createClientSocketAndRunHandlingMessages(
        node: Node,
    ): ISingleNodeConnectionHandler {
        val socket = clientSocketFactory.create(
            ip = node.ip,
            port = node.port,
        )
        val singleConnectionsHandler = singleNodeConnectionFactory.create(
            socket = socket,
        )

        val myId = getMyIdUseCase()
        singleConnectionsHandler.sendMyId(myId)

        singleConnectionsHandler.writeMessage(
            message = NodeMessage.Discovery(
                port = myPortRepository.getMyPortOrThrow().port,
                id = myId.id,
                name = "myName",
            ),
        )

        sockets.update { previousMap ->
            previousMap + (node.id to singleConnectionsHandler)
        }

        scope.launch {
            singleConnectionsHandler.listenIncomingMessages()
        }
        return singleConnectionsHandler
    }
}
