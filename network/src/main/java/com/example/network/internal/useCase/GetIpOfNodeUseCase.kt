package com.example.network.internal.useCase

import com.example.common.models.NodeId
import com.example.network.IGetIpOfNodeUseCase
import com.example.network.internal.data.nodes.IConnectionsHandler

internal class GetIpOfNodeUseCase(
    private val connectionHandler: IConnectionsHandler,
) : IGetIpOfNodeUseCase {

    override fun invoke(nodeId: NodeId): String? = connectionHandler
        .getIpOfSocket(nodeId = nodeId)
}
