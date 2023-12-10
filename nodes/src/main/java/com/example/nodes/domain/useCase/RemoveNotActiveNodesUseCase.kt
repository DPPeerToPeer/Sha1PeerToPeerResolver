package com.example.nodes.domain.useCase

import com.example.common.IGetCurrentTimeUseCase
import com.example.common.REMOVE_NODE_AFTER_INACTIVITY_DURATION
import com.example.nodes.data.repository.info.INodesInfoRepository
import kotlin.time.Duration.Companion.milliseconds

class RemoveNotActiveNodesUseCase(
    private val nodesInfoRepository: INodesInfoRepository,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
) {
    suspend operator fun invoke() {
        // Removing nodes which haven't reported their presence since timeToLive
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            val currentTime = getCurrentTimeUseCase().milliseconds
            val nodeHealth = nodesInfoRepository.getNodeHealth(node).lastSeen.milliseconds

            val nodeWasSeenThisTimeAgo = currentTime - nodeHealth

            if (nodeWasSeenThisTimeAgo >= REMOVE_NODE_AFTER_INACTIVITY_DURATION) nodesInfoRepository.removeNode(node.id)
        }
    }
}
