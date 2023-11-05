package org.example.sha1PeerToPeer.domain.useCases

import com.example.network.useCases.RunConnectionsHandlerUseCase
import org.example.sha1PeerToPeer.data.api.discovery.IDiscoveryApi
import org.example.sha1PeerToPeer.data.api.nodes.INodesApi
import org.example.sha1PeerToPeer.data.repository.nodes.INodesRepository

class DiscoveryUseCase(
    private val discoveryApi: IDiscoveryApi,
    private val nodesApi: INodesApi,
    private val runConnectionsHandlerUseCase: RunConnectionsHandlerUseCase,
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
