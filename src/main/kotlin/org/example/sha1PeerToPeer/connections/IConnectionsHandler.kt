package org.example.sha1PeerToPeer.connections

import kotlinx.coroutines.flow.Flow
import org.example.sha1PeerToPeer.domain.models.SocketId

interface IConnectionsHandler {

    suspend fun sendNodeMessage(message: Pair<SocketId, NodeMessage>)

    fun listenNodesMessages(): Flow<Pair<SocketId, NodeMessage>>
}
