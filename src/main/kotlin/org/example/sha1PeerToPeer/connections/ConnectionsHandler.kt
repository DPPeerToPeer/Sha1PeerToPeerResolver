package org.example.sha1PeerToPeer.connections

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.example.sha1PeerToPeer.domain.models.Node
import kotlin.coroutines.coroutineContext

class ConnectionsHandler : IConnectionsHandler {

    private val sockets: MutableStateFlow<List<NodeConnectionHandler>> = MutableStateFlow(emptyList())

    override suspend fun sendNodeMessage(message: Pair<Node, NodeMessage>) {
        TODO("Not yet implemented")
    }

    override fun listenNodesMessages(): Flow<Pair<Node, NodeMessage>> {
        TODO("Not yet implemented")
    }

    private fun onNewSocket(socket: Socket) {
        sockets.update {
            it + (NodeConnectionHandler(socket = socket))
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
