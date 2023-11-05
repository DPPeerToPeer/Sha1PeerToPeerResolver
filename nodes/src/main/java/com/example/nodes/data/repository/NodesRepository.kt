package com.example.nodes.data.repository

import com.example.common.models.SocketId
import com.example.nodes.domain.models.Node
import com.example.nodes.domain.models.NodeState
import kotlinx.coroutines.flow.MutableStateFlow

class NodesRepository : INodesRepository {

    private val nodes: MutableStateFlow<Map<Node, NodeState>> = MutableStateFlow(emptyMap())

    override suspend fun upsertManyNodes(nodes: List<Node.DiscoveredNode>) {
        TODO("Not yet implemented")
    }

    override suspend fun getActiveNodes(): List<Node> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertNode(node: Node) {
        TODO("Not yet implemented")
    }

    override suspend fun removeNode(id: SocketId) {
        TODO("Not yet implemented")
    }

    override suspend fun updateHealth(socketId: SocketId, timestamp: Long) {
        TODO("Not yet implemented")
    }
}
