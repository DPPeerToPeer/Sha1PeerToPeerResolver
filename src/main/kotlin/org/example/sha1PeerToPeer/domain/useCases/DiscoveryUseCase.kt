package org.example.sha1PeerToPeer.domain.useCases

import org.example.sha1PeerToPeer.connections.IConnectionsHandler
import org.example.sha1PeerToPeer.data.api.discovery.IDiscoveryApi
import org.example.sha1PeerToPeer.data.api.nodes.INodesApi
import org.example.sha1PeerToPeer.data.repository.nodes.INodesRepository

class DiscoveryUseCase(
    private val discoveryApi: IDiscoveryApi,
    private val nodesApi: INodesApi,
    private val connectionsHandler: IConnectionsHandler,
    private val nodesRepository: INodesRepository,
) {
    suspend operator fun invoke(
        hashToFind: String,
    ) {
        val myAddress = connectionsHandler.runAndReturnLocalIpAndPort()
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
