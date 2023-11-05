package com.example.nodes.domain.useCase

import com.example.common.models.GetCurrentTimeUseCase
import com.example.nodes.data.repository.broadcast.INodesBroadcastRepository

class SendHealthUseCase(
    private val nodesApi: INodesBroadcastRepository,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    suspend operator fun invoke() {
        nodesApi.sendHealth(timestamp = getCurrentTimeUseCase())
    }
}
