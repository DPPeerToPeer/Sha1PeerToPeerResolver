package com.example.network.internal.data.nodes

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import com.example.network.models.Port
import kotlinx.coroutines.flow.Flow

internal interface IConnectionsHandler {

    fun runAndReturnPort(): Port

    fun getIpOfSocket(nodeId: NodeId): String?

    suspend fun sendNodeMessage(nodeId: NodeId, message: NodeMessage)

    fun listenNodesMessages(): Flow<Pair<NodeId, NodeMessage>>
}
