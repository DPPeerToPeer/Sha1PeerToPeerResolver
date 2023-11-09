package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.nodes.domain.models.NodeState
import kotlinx.coroutines.flow.MutableStateFlow

internal class NodesInfoRepository : INodesInfoRepository {

    private val nodes: MutableStateFlow<Map<Node, NodeState>> = MutableStateFlow(emptyMap())

    override suspend fun upsertManyNodes(nodes: List<Node>) {
        TODO("Not yet implemented")
    }

    override suspend fun getActiveNodes(): List<Node> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertNode(node: Node) {
        TODO("Not yet implemented")
    }

    override suspend fun removeNode(id: NodeId) {
        TODO("Not yet implemented")
    }

    override suspend fun updateHealth(nodeId: NodeId, timestamp: Long) {
        TODO("Not yet implemented")
    }
}
