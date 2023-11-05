package org.example.sha1PeerToPeer.connections

import org.example.sha1PeerToPeer.domain.models.SocketId

class SendNodeMessageUseCase(
    private val connectionsHandler: IConnectionsHandler,
) {

    suspend operator fun invoke(message: Pair<SocketId, NodeMessage>) {
        connectionsHandler.sendNodeMessage(message = message)
    }
}
