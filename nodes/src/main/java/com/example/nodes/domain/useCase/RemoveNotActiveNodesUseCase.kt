package com.example.nodes.domain.useCase

import com.example.nodes.data.repository.info.INodesInfoRepository
import com.example.nodes.data.repository.info.NodesInfoRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toDuration


class RemoveNotActiveNodesUseCase(
    private val NodesInfoRepository: INodesInfoRepository,
    private val timeToLive: Duration = 1.minutes
) {
    suspend operator fun invoke() {
        // Removing nodes which haven't reported their presence since timeToLive
        var activeNodesList = NodesInfoRepository.getActiveNodes()

        for(node in activeNodesList){
            var currentTime = System.currentTimeMillis().milliseconds
            var nodeHealth =NodesInfoRepository.getNodeHealth(node).lastSeen.milliseconds

            var nodeWasSeenThisTimeAgo = currentTime - nodeHealth

            if(nodeWasSeenThisTimeAgo >= timeToLive) NodesInfoRepository.removeNode(node.id)
        }


    }
}
