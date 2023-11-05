package com.example.network.internal.data.nodes

import com.example.common.models.SocketId
import com.example.network.models.IpAndPort
import com.example.network.models.NodeMessage
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
    private val messageChannel: Channel<Pair<SocketId, NodeMessage>> = Channel(Channel.BUFFERED)

    override fun runAndReturnLocalIpAndPort(): IpAndPort {
        val selectorManager = SelectorManager(dispatcher = Dispatchers.IO)
        val serverSocket = aSocket(selectorManager).tcp().bind()

        scope.launch {
            while (coroutineContext.isActive) {
                val socket: Socket = serverSocket.accept()
                onNewSocket(socket)
            }
        }
        return serverSocket.localAddress.toJavaAddress().let {
            IpAndPort(
                ip = it.address,
                port = it.port,
            )
        }
    }

    override suspend fun sendNodeMessage(message: Pair<SocketId, NodeMessage>) {
        sockets.value.firstOrNull {
            message.first == it.socketId
        }?.writeMessage(message = message.second)
    }

    override fun listenNodesMessages(): Flow<Pair<SocketId, NodeMessage>> = messageChannel
        .receiveAsFlow()

    private fun onNewSocket(socket: Socket) {
        val singleConnectionsHandler = SingleNodeConnectionHandler(
            socket = socket,
            messageChannel = messageChannel,
            socketId = SocketId(id = UUID.randomUUID().toString()),
        )
        sockets.update {
            it + singleConnectionsHandler
        }
        scope.launch {
            singleConnectionsHandler.listenIncomingMessages()
        }
    }
}
