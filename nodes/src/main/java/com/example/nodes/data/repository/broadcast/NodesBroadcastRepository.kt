package com.example.nodes.data.repository.broadcast

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.network.ISendNodeMessageUseCase
import com.example.network.models.NodeMessage
import com.example.nodes.data.repository.info.INodesInfoRepository
import com.example.nodes.data.repository.info.NodesInfoRepository

class NodesBroadcastRepository(
    private val sendNodeMessageUseCase: ISendNodeMessageUseCase,
    private val NodesInfoRepository: INodesInfoRepository
): INodesBroadcastRepository {

    // 1. Pobieram listę aktywnych nodów. 2. Dla każdego node zawołać metodę invoke z SendNodeMessageUseCase.
    override suspend fun sendMyInfo(port: Int) {
        TODO("Yet not implemented")// Tego jeszcze nie robic


    }

    override suspend fun sendStart() {
        TODO("Not yet implemented")// Tego jeszcze nie robic

    }

    override suspend fun sendStartedCalculation(batch: Batch, timestamp: Long) {
        // Invoke method from sendNodeMessageUseCase for every node sending NodeMessage.StartedCalculation message
        var message = NodeMessage.StartedCalculation(batch, timestamp)
        var avtiveNodesList = NodesInfoRepository.getActiveNodes()

        for (node in avtiveNodesList){
            sendNodeMessageUseCase.invoke(node.id,message)
        }
    }

    override suspend fun sendEndedCalculation(batch: Batch, calculationResult: CalculationResult) {
        var message = NodeMessage.EndedCalculation(batch, calculationResult)
        var avtiveNodesList = NodesInfoRepository.getActiveNodes()

        for (node in avtiveNodesList){
            sendNodeMessageUseCase.invoke(node.id,message)
        }
    }

    override suspend fun sendHealth(timestamp: Long) {
        var message = NodeMessage.Health(timestamp)
        var avtiveNodesList = NodesInfoRepository.getActiveNodes()

        for (node in avtiveNodesList){
            sendNodeMessageUseCase.invoke(node.id,message)
        }
    }
}