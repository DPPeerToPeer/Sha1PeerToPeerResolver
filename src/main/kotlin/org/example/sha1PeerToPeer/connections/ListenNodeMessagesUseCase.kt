package org.example.sha1PeerToPeer.connections

import kotlinx.coroutines.flow.Flow
import org.example.sha1PeerToPeer.domain.models.Node

class ListenNodeMessagesUseCase(
    private val connectionsHandler: ConnectionsHandler,
) {

    operator fun invoke(): Flow<Pair<Node, NodeMessage>> = connectionsHandler
        .listenNodesMessages()
}
