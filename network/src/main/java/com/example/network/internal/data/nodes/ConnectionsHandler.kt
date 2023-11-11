package com.example.network.internal.data.nodes

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import com.example.network.models.Port
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

internal class ConnectionsHandler(
    private val scope: CoroutineScope,
) : IConnectionsHandler {

    private val sockets: MutableStateFlow<Map<NodeId, SingleNodeConnectionHandler>> = MutableStateFlow(emptyMap())
    private val messageChannel: Channel<Pair<NodeId, NodeMessage>> = Channel(Channel.BUFFERED)

    override fun runAndReturnPort(): Port {
        val selectorManager = SelectorManager(dispatcher = Dispatchers.IO)
        val serverSocket = aSocket(selectorManager).tcp().bind()

        scope.launch {
            while (coroutineContext.isActive) {
                val socket: Socket = serverSocket.accept()
                launch {
                    runHandlingNewSocket(socket)
                }
            }
        }
        return serverSocket.localAddress.toJavaAddress().let {
            Port(port = it.port)
        }
    }

    override suspend fun sendNodeMessage(nodeId: NodeId, message: NodeMessage) {
        sockets.value[nodeId]?.writeMessage(message = message)
    }

    override fun listenNodesMessages(): Flow<Pair<NodeId, NodeMessage>> = messageChannel
        .receiveAsFlow()

    private suspend fun runHandlingNewSocket(socket: Socket) {
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
