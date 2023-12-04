package com.example.network.internal.data.discovery

import com.example.common.models.Node
import com.example.common.models.NodeId
import kotlinx.coroutines.flow.Flow

internal interface IDiscoveryApi {

    fun runDiscoveryAndListenNewNodes(
        hashToFind: String,
        myId: NodeId,
        myName: String,
        myPort: Int,
    ): Flow<Node>
}
