package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.SocketId

interface INodesInfoRepository {

    suspend fun upsertManyNodes(nodes: List<Node.DiscoveredNode>)

    suspend fun getActiveNodes(): List<Node>

    suspend fun upsertNode(node: Node)

    suspend fun removeNode(id: SocketId)

    suspend fun updateHealth(
        socketId: SocketId,
        timestamp: Long,
    )
}
