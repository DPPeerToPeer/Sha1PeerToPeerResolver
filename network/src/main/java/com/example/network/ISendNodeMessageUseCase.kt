package com.example.network

import com.example.common.models.SocketId
import com.example.network.models.NodeMessage

interface ISendNodeMessageUseCase {
    suspend operator fun invoke(message: Pair<SocketId, NodeMessage>)
}
