package com.example.network.internal.data.nodes

import com.example.common.IGetMyIdUseCase
import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.messagesProxy.IMessagesProxy
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionFactory
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionHandler
import com.example.network.models.NodeMessage
import com.example.network.models.Port
import com.example.socketsFacade.IClientSocketFactory
import com.example.socketsFacade.IReadWriteSocket
import com.example.socketsFacade.IServerSocketFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.IllegalStateException
import kotlin.coroutines.coroutineContext

internal class ConnectionsHandler(
    private val scope: CoroutineScope,
    private val serverSocketFactory: IServerSocketFactory,
    private val singleNodeConnectionFactory: ISingleNodeConnectionFactory,
    private val messagesProxy: IMessagesProxy,
    private val clientSocketFactory: IClientSocketFactory,
    private val getMyIdUseCase: IGetMyIdUseCase,
) : IConnectionsHandler {

    private val sockets: MutableStateFlow<Map<NodeId, ISingleNodeConnectionHandler>> = MutableStateFlow(emptyMap())
    private var myPort: Port? = null

    override fun runAndReturnPort(): Port {
        val serverSocket = serverSocketFactory.create()

        scope.launch {
            supervisorScope {
                while (coroutineContext.isActive) {
                    val socket: IReadWriteSocket = serverSocket.accept()
                    launch {
                        runHandlingNewSocket(socket)
                    }
                }
            }
        }
        return Port(port = serverSocket.port).also { myPort = it }
    }

    override fun getIpOfSocket(nodeId: NodeId): String? =
        sockets.value[nodeId]?.socketIp

    override suspend fun sendNodeMessage(node: Node, message: NodeMessage) {
        runCatching {
            val connectionHandler = sockets.value[node.id] ?: createClientSocketAndRunHandlingMessages(node)

            connectionHandler.writeMessage(message = message)
        }.onFailure {
            coroutineContext.ensureActive()
        }
    }

    override fun listenNodesMessages(): Flow<Pair<NodeId, NodeMessage>> = messagesProxy
        .listenMessages()

    private suspend fun runHandlingNewSocket(socket: IReadWriteSocket) {
        val singleConnectionsHandler = singleNodeConnectionFactory.create(
            socket = socket,
        )
        val nodeId = singleConnectionsHandler.listenNodeId()
        sockets.update { previousMap ->
            previousMap + (nodeId to singleConnectionsHandler)
        }

        singleConnectionsHandler.listenIncomingMessages()
    }

    private suspend fun createClientSocketAndRunHandlingMessages(
        node: Node,
    ): ISingleNodeConnectionHandler {
        checkNotNull(myPort)

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
                port = myPort?.port ?: throw IllegalStateException(),
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
