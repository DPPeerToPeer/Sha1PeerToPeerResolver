package org.example.sha1PeerToPeer.data.repository.nodes

import kotlinx.coroutines.flow.MutableStateFlow
import org.example.sha1PeerToPeer.domain.models.Node
import org.example.sha1PeerToPeer.domain.models.NodeState
import org.example.sha1PeerToPeer.domain.models.SocketId

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
