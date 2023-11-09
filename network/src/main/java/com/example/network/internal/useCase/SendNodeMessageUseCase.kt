package com.example.network.internal.useCase

import com.example.common.models.NodeId
import com.example.network.ISendNodeMessageUseCase
import com.example.network.internal.data.nodes.IConnectionsHandler
import com.example.network.models.NodeMessage

internal class SendNodeMessageUseCase(
    private val connectionsHandler: IConnectionsHandler,
) : ISendNodeMessageUseCase {

    override suspend operator fun invoke(message: Pair<NodeId, NodeMessage>) {
        connectionsHandler.sendNodeMessage(message = message)
    }
}
