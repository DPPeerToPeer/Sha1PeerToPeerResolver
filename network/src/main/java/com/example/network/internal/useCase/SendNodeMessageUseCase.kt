package com.example.network.internal.useCase

import com.example.common.models.Node
import com.example.network.ISendNodeMessageUseCase
import com.example.network.internal.data.nodes.IConnectionsHandler
import com.example.network.models.NodeMessage

internal class SendNodeMessageUseCase(
    private val connectionsHandler: IConnectionsHandler,
) : ISendNodeMessageUseCase {

    override suspend operator fun invoke(node: Node, message: NodeMessage) {
        connectionsHandler.sendNodeMessage(
            node = node,
            message = message,
        )
    }
}
