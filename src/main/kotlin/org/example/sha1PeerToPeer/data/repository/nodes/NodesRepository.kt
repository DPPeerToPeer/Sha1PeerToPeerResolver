package org.example.sha1PeerToPeer.data.repository.nodes

import kotlinx.coroutines.flow.MutableStateFlow
import org.example.sha1PeerToPeer.domain.models.Node
import org.example.sha1PeerToPeer.domain.models.NodeState

class NodesRepository : INodesRepository {

    private val nodes: MutableStateFlow<Map<Node, NodeState>> = MutableStateFlow(emptyMap())

    override suspend fun addNewNode(node: Node) {
        TODO("Not yet implemented")
    }

    override suspend fun removeNode(node: Node) {
        TODO("Not yet implemented")
    }

    override suspend fun updateHealth(node: Node, timestamp: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun sendBroadcastToAllNodes() {
        TODO("Not yet implemented")
    }
}
