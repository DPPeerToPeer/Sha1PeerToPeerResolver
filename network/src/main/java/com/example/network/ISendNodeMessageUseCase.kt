package com.example.network

import com.example.common.models.Node
import com.example.network.models.NodeMessage

interface ISendNodeMessageUseCase {

    suspend operator fun invoke(node: Node, message: NodeMessage)
}
