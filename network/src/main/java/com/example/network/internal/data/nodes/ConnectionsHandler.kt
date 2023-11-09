package com.example.network.internal.data.nodes

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import com.example.network.models.Port
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

internal class ConnectionsHandler(
    private val scope: CoroutineScope,
) : IConnectionsHandler {

    private val sockets: MutableStateFlow<List<SingleNodeConnectionHandler>> = MutableStateFlow(emptyList())
    private val messageChannel: Channel<Pair<NodeId, NodeMessage>> = Channel(Channel.BUFFERED)

    override fun runAndReturnPort(): Port {
        val selectorManager = SelectorManager(dispatcher = Dispatchers.IO)
        val serverSocket = aSocket(selectorManager).tcp().bind()

        scope.launch {
            while (coroutineContext.isActive) {
                val socket: Socket = serverSocket.accept()
                onNewSocket(socket)
            }
        }
        return serverSocket.localAddress.toJavaAddress().let {
            Port(port = it.port)
        }
    }

    override suspend fun sendNodeMessage(message: Pair<NodeId, NodeMessage>) {
        sockets.value.firstOrNull {
            message.first == it.nodeId
        }?.writeMessage(message = message.second)
    }

    override fun listenNodesMessages(): Flow<Pair<NodeId, NodeMessage>> = messageChannel
        .receiveAsFlow()

    private fun onNewSocket(socket: Socket) {
        val singleConnectionsHandler = SingleNodeConnectionHandler(
            socket = socket,
            messageChannel = messageChannel,
            nodeId = NodeId(id = UUID.randomUUID().toString()),
        )
        sockets.update {
            it + singleConnectionsHandler
        }
        scope.launch {
            singleConnectionsHandler.listenIncomingMessages()
        }
    }
}
