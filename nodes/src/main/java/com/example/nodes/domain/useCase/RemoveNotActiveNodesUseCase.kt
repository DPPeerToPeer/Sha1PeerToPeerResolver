package com.example.nodes.domain.useCase

import com.example.common.IGetCurrentTimeUseCase
import com.example.common.REMOVE_NODE_AFTER_INACTIVITY_DURATION
import com.example.common.models.Node
import com.example.nodes.IRemoveNotActiveNodesUseCase
import com.example.nodes.data.repository.info.INodesInfoRepository
import kotlin.time.Duration.Companion.milliseconds

internal class RemoveNotActiveNodesUseCase(
    private val nodesInfoRepository: INodesInfoRepository,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
) : IRemoveNotActiveNodesUseCase {

    override suspend operator fun invoke(): List<Node> {
        // Remove nodes which haven't reported their presence since REMOVE_NODE_AFTER_INACTIVITY_DURATION
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        val removedNodes = mutableListOf<Node>()
        for (node in activeNodesList) {
            val currentTime = getCurrentTimeUseCase().milliseconds
            val nodeHealth = nodesInfoRepository.getNodeHealth(node).lastSeen.milliseconds

            val nodeWasSeenThisTimeAgo = currentTime - nodeHealth

            if (nodeWasSeenThisTimeAgo >= REMOVE_NODE_AFTER_INACTIVITY_DURATION) {
                nodesInfoRepository.removeNode(node.id)
                removedNodes.add(node)
            }
        }

        return removedNodes
    }
}
