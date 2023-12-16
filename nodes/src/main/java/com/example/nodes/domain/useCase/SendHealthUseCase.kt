package com.example.nodes.domain.useCase

import com.example.common.IGetCurrentTimeUseCase
import com.example.nodes.INodesBroadcastUseCase
import com.example.nodes.ISendHealthUseCase

internal class SendHealthUseCase(
    private val nodesBroadcastUseCase: INodesBroadcastUseCase,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
) : ISendHealthUseCase {

    override suspend operator fun invoke() {
        nodesBroadcastUseCase.sendHealth(timestamp = getCurrentTimeUseCase())
    }
}
