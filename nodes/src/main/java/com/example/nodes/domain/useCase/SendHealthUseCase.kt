package com.example.nodes.domain.useCase

import com.example.common.models.GetCurrentTimeUseCase
import com.example.nodes.data.api.INodesBroadcastApi

class SendHealthUseCase(
    private val nodesApi: INodesBroadcastApi,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) {

    suspend operator fun invoke() {
        nodesApi.sendHealth(timestamp = getCurrentTimeUseCase())
    }
}
