package com.example.network.internal.data.nodes

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import com.example.network.models.Port
import com.example.socketsFacade.IReadWriteSocket
import com.example.socketsFacade.IServerSocketFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

internal class ConnectionsHandler(
    private val scope: CoroutineScope,
    private val serverSocketFactory: IServerSocketFactory,
) : IConnectionsHandler {

    private val sockets: MutableStateFlow<Map<NodeId, SingleNodeConnectionHandler>> = MutableStateFlow(emptyMap())
    private val messageChannel: Channel<Pair<NodeId, NodeMessage>> = Channel(Channel.BUFFERED)

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
        return Port(port = serverSocket.port)
    }

    override suspend fun sendNodeMessage(nodeId: NodeId, message: NodeMessage) {
        sockets.value[nodeId]?.writeMessage(message = message)
    }

    override fun listenNodesMessages(): Flow<Pair<NodeId, NodeMessage>> = messageChannel
        .receiveAsFlow()

    private suspend fun runHandlingNewSocket(socket: IReadWriteSocket) {
        val singleConnectionsHandler = SingleNodeConnectionHandler(
            socket = socket,
            messageChannel = messageChannel,
        )
        val nodeId = singleConnectionsHandler.listenNodeId()
        sockets.update { previousMap ->
            previousMap + (nodeId to singleConnectionsHandler)
        }

        singleConnectionsHandler.listenIncomingMessages()
    }
}
