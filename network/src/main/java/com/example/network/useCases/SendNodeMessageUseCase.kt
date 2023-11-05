package com.example.network.useCases

import com.example.common.models.SocketId
import com.example.network.IConnectionsHandler
import com.example.network.NodeMessage

class SendNodeMessageUseCase(
    private val connectionsHandler: IConnectionsHandler,
) {

    suspend operator fun invoke(message: Pair<SocketId, NodeMessage>) {
        connectionsHandler.sendNodeMessage(message = message)
    }
}
