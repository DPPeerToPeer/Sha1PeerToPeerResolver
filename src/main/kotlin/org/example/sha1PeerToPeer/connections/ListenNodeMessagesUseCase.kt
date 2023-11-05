package org.example.sha1PeerToPeer.connections

import kotlinx.coroutines.flow.Flow
import org.example.sha1PeerToPeer.domain.models.SocketId

class ListenNodeMessagesUseCase(
    private val connectionsHandler: IConnectionsHandler,
) {

    operator fun invoke(): Flow<Pair<SocketId, NodeMessage>> = connectionsHandler
        .listenNodesMessages()
}
