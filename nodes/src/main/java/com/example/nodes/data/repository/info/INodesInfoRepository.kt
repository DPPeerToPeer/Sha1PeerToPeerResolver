package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.nodes.domain.models.NodeState
import kotlinx.coroutines.flow.Flow

interface INodesInfoRepository {

    // If Node.id exists in nodes node is being swapped. Else it is being added.
    suspend fun upsertManyNodes(nodes: List<Node>)

    // Method returns nodes
    suspend fun getActiveNodes(): List<Node>

    suspend fun getActiveNodesFlow(): Flow<List<Node>>

    // Adds single node to nodes
    suspend fun upsertNode(node: Node)

    // Removes single node from nodes
    suspend fun removeNode(id: NodeId)

    // Swaps timestamp for node with given nodeId
    suspend fun updateHealth(
        nodeId: NodeId,
        timestamp: Long,
    )

    // Returns given node health
    suspend fun getNodeHealth(node: Node): NodeState
}
