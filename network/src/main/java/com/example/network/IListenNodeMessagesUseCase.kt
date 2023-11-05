package com.example.network

import com.example.common.models.SocketId
import com.example.network.models.NodeMessage
import kotlinx.coroutines.flow.Flow

interface IListenNodeMessagesUseCase {
    operator fun invoke(): Flow<Pair<SocketId, NodeMessage>>
}
