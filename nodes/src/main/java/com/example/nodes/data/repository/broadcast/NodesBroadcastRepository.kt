package com.example.nodes.data.repository.broadcast

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.network.ISendNodeMessageUseCase
import com.example.network.models.NodeMessage
import com.example.nodes.data.repository.info.INodesInfoRepository

class NodesBroadcastRepository(
    private val sendNodeMessageUseCase: ISendNodeMessageUseCase,
    private val nodesInfoRepository: INodesInfoRepository,
) : INodesBroadcastRepository {

    override suspend fun sendMyInfo(port: Int) {
        TODO("Yet not implemented") // In progress
    }

    override suspend fun sendStart() {
        TODO("Not yet implemented") // In progress
    }

    override suspend fun sendStartedCalculation(batch: Batch, timestamp: Long) {
        // Invokes method from sendNodeMessageUseCase on every node sending NodeMessage.StartedCalculation message
        val message = NodeMessage.StartedCalculation(batch, timestamp)
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            sendNodeMessageUseCase.invoke(node, message)
        }
    }

    override suspend fun sendEndedCalculation(batch: Batch, calculationResult: CalculationResult) {
        // Invokes method from sendNodeMessageUseCase on every node sending NodeMessage.EndedCalculation message
        val message = NodeMessage.EndedCalculation(batch, calculationResult)
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            sendNodeMessageUseCase.invoke(node, message)
        }
    }

    override suspend fun sendHealth(timestamp: Long) {
        // Invokes method from sendNodeMessageUseCase on every node sending NodeMessage.Health message
        val message = NodeMessage.Health(timestamp)
        val activeNodesList = nodesInfoRepository.getActiveNodes()

        for (node in activeNodesList) {
            sendNodeMessageUseCase.invoke(node, message)
        }
    }
}
