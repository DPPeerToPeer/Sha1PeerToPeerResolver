package com.example.network.internal.useCase

import com.example.common.IGetMyIdUseCase
import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.IDiscoveryUseCase
import com.example.network.internal.data.discovery.IDiscoveryApi
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach

private val logger = KotlinLogging.logger {}

internal class DiscoveryUseCase(
    private val discoveryApi: IDiscoveryApi,
    private val getMyIdUseCase: IGetMyIdUseCase,
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
    ).filter {
        it.id != getMyIdUseCase()
    }.onEach {
        logger.atDebug {
            message = "new node"
            payload = buildMap {
                put("node", it)
            }
        }
    }
}
