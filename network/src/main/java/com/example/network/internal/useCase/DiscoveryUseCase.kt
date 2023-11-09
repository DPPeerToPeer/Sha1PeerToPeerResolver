package com.example.network.internal.useCase

import com.example.common.models.Node
import com.example.network.IDiscoveryUseCase
import com.example.network.internal.data.discovery.IDiscoveryApi

internal class DiscoveryUseCase(
    private val discoveryApi: IDiscoveryApi,
) : IDiscoveryUseCase {
    override suspend operator fun invoke(
        myPort: Int,
        hashToFind: String,
    ): List<Node> = discoveryApi.joinToSession(
        hashToFind = hashToFind,
        myPort = myPort,
    )
}
