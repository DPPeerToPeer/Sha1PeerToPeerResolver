package org.example.sha1PeerToPeer.domain.useCases

import org.example.sha1PeerToPeer.data.api.nodes.INodesApi

class SendHealthUseCase(
    private val nodesApi: INodesApi,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    suspend operator fun invoke() {
        nodesApi.sendHealth(timestamp = getCurrentTimeUseCase())
    }
}
