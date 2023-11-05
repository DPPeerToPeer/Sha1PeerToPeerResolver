package com.example.network.useCases

import com.example.common.models.SocketId
import com.example.network.ISendNodeMessageUseCase
import com.example.network.internal.IConnectionsHandler
import com.example.network.models.NodeMessage

internal class SendNodeMessageUseCase(
    private val connectionsHandler: IConnectionsHandler,
) : ISendNodeMessageUseCase {

    override suspend operator fun invoke(message: Pair<SocketId, NodeMessage>) {
        connectionsHandler.sendNodeMessage(message = message)
    }
}
