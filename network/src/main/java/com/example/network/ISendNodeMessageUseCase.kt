package com.example.network

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage

interface ISendNodeMessageUseCase {
    suspend operator fun invoke(nodeId: NodeId, message: NodeMessage)
}
