package org.example.sha1PeerToPeer.connections

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import org.example.sha1PeerToPeer.domain.models.SocketId
import java.util.UUID
import kotlin.coroutines.coroutineContext

class ConnectionsHandler(
    private val scope: CoroutineScope,
) : IConnectionsHandler {

    private val sockets: MutableStateFlow<List<SingleNodeConnectionHandler>> = MutableStateFlow(emptyList())
    private val messageChannel: Channel<Pair<SocketId, NodeMessage>> = Channel(Channel.BUFFERED)

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

    suspend fun run() {
        val selectorManager = SelectorManager(dispatcher = Dispatchers.IO)
        val serverSocket = aSocket(selectorManager).tcp().bind()

        while (coroutineContext.isActive) {
            val socket: Socket = serverSocket.accept()
            onNewSocket(socket)
        }
    }
}
