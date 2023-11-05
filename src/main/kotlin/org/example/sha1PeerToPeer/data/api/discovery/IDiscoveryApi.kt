package org.example.sha1PeerToPeer.data.api.discovery

import com.example.nodes.domain.models.Node

interface IDiscoveryApi {

    suspend fun joinToSession(
        hashToFind: String,
        myPort: Int,
    ): List<Node.DiscoveredNode>
}
