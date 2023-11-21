package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.NodeId

interface INodesInfoRepository {

    suspend fun upsertManyNodes(nodes: List<Node>)//TODO

    suspend fun getActiveNodes(): List<Node>//TODO

    suspend fun upsertNode(node: Node)//TODO

    suspend fun removeNode(id: NodeId)//TODO

    suspend fun updateHealth(//TODO
        nodeId: NodeId,
        timestamp: Long,
    )
}
