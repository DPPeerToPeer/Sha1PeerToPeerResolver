package org.example.sha1PeerToPeer.data.repository.nodes

import com.example.common.models.SocketId
import org.example.sha1PeerToPeer.domain.models.Node

interface INodesRepository {

    suspend fun upsertManyNodes(nodes: List<Node.DiscoveredNode>)

    suspend fun getActiveNodes(): List<Node>

    suspend fun upsertNode(node: Node)

    suspend fun removeNode(id: SocketId)

    suspend fun updateHealth(
        socketId: SocketId,
        timestamp: Long,
    )
}
