package com.example.nodes.domain.useCase

import com.example.common.IGetCurrentTimeUseCase
import com.example.nodes.data.repository.broadcast.INodesBroadcastRepository

class SendHealthUseCase(
    private val nodesApi: INodesBroadcastRepository,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
) {

    suspend operator fun invoke() {
        nodesApi.sendHealth(timestamp = getCurrentTimeUseCase())
    }
}
