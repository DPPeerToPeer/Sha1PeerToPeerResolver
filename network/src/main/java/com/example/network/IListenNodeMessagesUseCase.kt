package com.example.network

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import kotlinx.coroutines.flow.Flow

interface IListenNodeMessagesUseCase {
    operator fun invoke(): Flow<Pair<NodeId, NodeMessage>>
}
