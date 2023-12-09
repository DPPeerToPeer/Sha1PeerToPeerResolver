package com.example.nodes.domain.useCase

import com.example.common.IGetCurrentTimeUseCase
import com.example.nodes.data.repository.info.INodesInfoRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class RemoveNotActiveNodesUseCase(
    private val nodesInfoRepository: INodesInfoRepository,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
    private val timeToLive: Duration = 1.minutes,
) {
    suspend operator fun invoke() {
        // Removing nodes which haven't reported their presence since timeToLive
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            val currentTime = getCurrentTimeUseCase().milliseconds
            val nodeHealth = nodesInfoRepository.getNodeHealth(node).lastSeen.milliseconds

            val nodeWasSeenThisTimeAgo = currentTime - nodeHealth

            if (nodeWasSeenThisTimeAgo >= timeToLive) nodesInfoRepository.removeNode(node.id)
        }
    }
}
