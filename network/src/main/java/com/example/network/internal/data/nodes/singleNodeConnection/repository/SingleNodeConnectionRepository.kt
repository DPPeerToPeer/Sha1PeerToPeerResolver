package com.example.network.internal.data.nodes.singleNodeConnection.repository

import com.example.common.IGetMyIdUseCase
import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.myPort.IMyPortRepository
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionHandler
import com.example.network.internal.data.nodes.singleNodeConnection.factory.ISingleNodeConnectionFactory
import com.example.network.models.NodeMessage
import com.example.socketsFacade.IClientSocketFactory
import com.example.socketsFacade.IReadWriteSocket
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val logger = KotlinLogging.logger {}

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
        logger.debug {
            "createSingleConnectionHandlerAsServer listenNodeId"
        }
        val nodeId = singleConnectionsHandler.listenNodeId()
        logger.atDebug {
            message = "listenedId"
            payload = buildMap {
                put("nodeId", nodeId)
            }
        }
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
        logger.debug {
            "createClientSocketAndRunHandlingMessages"
        }
        val socket = clientSocketFactory.create(
            ip = node.ip,
            port = node.port,
        )
        val singleConnectionsHandler = singleNodeConnectionFactory.create(
            socket = socket,
        )

        val myId = getMyIdUseCase()
        singleConnectionsHandler.sendMyId(myId)

        logger.atDebug {
            message = "sendMyId"
            payload = buildMap {
                put("id", myId)
            }
        }

        val discoveryMessage = NodeMessage.Discovery(
            port = myPortRepository.getMyPortOrThrow().port,
            id = myId.id,
            name = "myName",
        )

        singleConnectionsHandler.writeMessage(
            message = discoveryMessage,
        )

        logger.atDebug {
            message = "send discovery"
            payload = buildMap {
                put("message", discoveryMessage)
            }
        }

        sockets.update { previousMap ->
            previousMap + (node.id to singleConnectionsHandler)
        }

        scope.launch {
            singleConnectionsHandler.listenIncomingMessages()
        }
        return singleConnectionsHandler
    }
}
