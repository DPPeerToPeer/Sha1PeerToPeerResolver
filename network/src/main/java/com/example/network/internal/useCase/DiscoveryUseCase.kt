package com.example.network.internal.useCase

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.IDiscoveryUseCase
import com.example.network.internal.data.discovery.IDiscoveryApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

internal class DiscoveryUseCase(
    private val discoveryApi: IDiscoveryApi,
) : IDiscoveryUseCase {
    override operator fun invoke(
        myPort: Int,
        myId: NodeId,
        hashToFind: String,
        myName: String,
    ): Flow<Node> = discoveryApi.runDiscoveryAndListenNewNodes(
        hashToFind = hashToFind,
        myPort = myPort,
        myId = myId,
        myName = myName,
    ).onEach(::println)
}
