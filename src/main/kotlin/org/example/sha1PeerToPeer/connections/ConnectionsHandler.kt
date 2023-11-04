package org.example.sha1PeerToPeer.connections

import kotlinx.coroutines.flow.Flow
import org.example.sha1PeerToPeer.domain.models.Node

class ConnectionsHandler : IConnectionsHandler {
    override suspend fun sendNodeMessage(message: Pair<Node, NodeMessage>) {
        TODO("Not yet implemented")
    }

    override fun listenNodesMessages(): Flow<Pair<Node, NodeMessage>> {
        TODO("Not yet implemented")
    }
}
