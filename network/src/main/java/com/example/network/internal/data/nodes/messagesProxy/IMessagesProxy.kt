package com.example.network.internal.data.nodes.messagesProxy

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import kotlinx.coroutines.flow.Flow

internal interface IMessagesProxy {

    suspend fun onNewMessage(
        nodeId: NodeId,
        message: NodeMessage,
    )

    fun listenMessages(): Flow<Pair<NodeId, NodeMessage>>
}
