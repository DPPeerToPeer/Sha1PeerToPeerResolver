package com.example.nodes.domain.useCase

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.network.ISendNodeMessageUseCase
import com.example.network.models.NodeMessage
import com.example.nodes.INodesBroadcastUseCase
import com.example.nodes.data.repository.info.INodesInfoRepository

internal class NodesBroadcastUseCase(
    private val sendNodeMessageUseCase: ISendNodeMessageUseCase,
    private val nodesInfoRepository: INodesInfoRepository,
) : INodesBroadcastUseCase {

    override suspend fun sendStart() {
        TODO("Not yet implemented")
    }

    override suspend fun sendStartedCalculation(batch: Batch, timestamp: Long) {
        val message = NodeMessage.StartedCalculation(batch, timestamp)
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            sendNodeMessageUseCase.invoke(node, message)
        }
    }

    override suspend fun sendEndedCalculation(batch: Batch, calculationResult: CalculationResult) {
        val message = NodeMessage.EndedCalculation(batch, calculationResult)
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            sendNodeMessageUseCase.invoke(node, message)
        }
    }

    override suspend fun sendHealth(timestamp: Long) {
        val message = NodeMessage.Health(timestamp)
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            sendNodeMessageUseCase.invoke(node, message)
        }
    }
}
