package com.example.network

import com.example.common.models.Node
import com.example.common.models.NodeId
import kotlinx.coroutines.flow.Flow

interface IDiscoveryUseCase {

    operator fun invoke(
        myPort: Int,
        myId: NodeId,
        hashToFind: String,
        myName: String,
    ): Flow<Node>
}
