package org.example.sha1PeerToPeer.domain.useCases

import com.example.network.IRunConnectionsHandlerUseCase
import com.example.nodes.data.api.INodesApi
import com.example.nodes.data.repository.INodesRepository
import org.example.sha1PeerToPeer.data.api.discovery.IDiscoveryApi

class DiscoveryUseCase(
    private val discoveryApi: IDiscoveryApi,
    private val nodesApi: INodesApi,
    private val runConnectionsHandlerUseCase: IRunConnectionsHandlerUseCase,
    private val nodesRepository: INodesRepository,
) {
    suspend operator fun invoke(
        hashToFind: String,
    ) {
        val myAddress = runConnectionsHandlerUseCase.invoke()
        val currentNodes = discoveryApi.joinToSession(
            hashToFind = hashToFind,
            myPort = myAddress.port,
        )

        nodesRepository.upsertManyNodes(nodes = currentNodes)

        nodesApi.sendMyInfo(
            port = myAddress.port,
            ip = myAddress.ip,
        )
    }
}
