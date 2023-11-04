package org.example.sha1PeerToPeer.connections

import org.example.sha1PeerToPeer.domain.models.Node

class SendNodeMessageUseCase(
    private val connectionsHandler: ConnectionsHandler,
) {

    suspend operator fun invoke(message: Pair<Node, NodeMessage>) {
        connectionsHandler.sendNodeMessage(message = message)
    }
}
