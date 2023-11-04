package org.example.sha1PeerToPeer.data.api.discovery

import org.example.sha1PeerToPeer.domain.models.Node

interface IDiscoveryApi {

    suspend fun joinToSession(
        hashToFind: String,
        myPort: Int,
    ): List<Node>
}
