package com.example.network

import com.example.common.models.NodeId

interface IGetIpOfNodeUseCase {

    operator fun invoke(nodeId: NodeId): String?
}
