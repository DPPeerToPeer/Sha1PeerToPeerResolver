package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.NodeId

interface INodesInfoRepository {

    suspend fun upsertManyNodes(nodes: List<Node>)

    suspend fun getActiveNodes(): List<Node>

    suspend fun upsertNode(node: Node)

    suspend fun removeNode(id: NodeId)

    suspend fun updateHealth(
        nodeId: NodeId,
        timestamp: Long,
    )
}
