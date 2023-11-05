package com.example.network.internal

import com.example.common.models.Node

internal interface IDiscoveryApi {

    suspend fun joinToSession(
        hashToFind: String,
        myPort: Int,
    ): List<Node.DiscoveredNode>
}
