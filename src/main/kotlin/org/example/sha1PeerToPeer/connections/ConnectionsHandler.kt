package org.example.sha1PeerToPeer.connections

import kotlinx.coroutines.flow.Flow
import org.example.sha1PeerToPeer.domain.models.Node

interface ConnectionsHandler {

    suspend fun sendNodeMessage(message: Pair<Node, NodeMessage>)

    fun listenNodesMessages(): Flow<Pair<Node, NodeMessage>>
}
