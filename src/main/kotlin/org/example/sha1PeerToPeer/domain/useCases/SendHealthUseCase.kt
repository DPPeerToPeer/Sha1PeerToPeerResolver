package org.example.sha1PeerToPeer.domain.useCases

import com.example.nodes.data.api.INodesApi

class SendHealthUseCase(
    private val nodesApi: INodesApi,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    suspend operator fun invoke() {
        nodesApi.sendHealth(timestamp = getCurrentTimeUseCase())
    }
}
