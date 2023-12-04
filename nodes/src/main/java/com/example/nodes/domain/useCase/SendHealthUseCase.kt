package com.example.nodes.domain.useCase

import com.example.common.models.GetCurrentTimeUseCase
import com.example.nodes.data.repository.broadcast.INodesBroadcastRepository

class SendHealthUseCase(//TODO Naraznie nie implementować
    private val nodesApi: INodesBroadcastRepository,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    suspend operator fun invoke() {//TODO Naraznie nie implementować
        nodesApi.sendHealth(timestamp = getCurrentTimeUseCase())
    }
}
