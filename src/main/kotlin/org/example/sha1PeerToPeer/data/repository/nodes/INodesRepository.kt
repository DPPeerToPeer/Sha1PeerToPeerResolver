package org.example.sha1PeerToPeer.data.repository.nodes

import org.example.sha1PeerToPeer.domain.models.Node

interface INodesRepository {

    suspend fun addNewNode(node: Node)

    suspend fun removeNode(node: Node)

    suspend fun updateHealth(
        node: Node,
        timestamp: Long,
    )

    suspend fun sendBroadcastToAllNodes()
}
