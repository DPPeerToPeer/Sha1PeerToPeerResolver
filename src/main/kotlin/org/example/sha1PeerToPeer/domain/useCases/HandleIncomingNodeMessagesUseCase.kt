package org.example.sha1PeerToPeer.domain.useCases

import com.example.calculation.ICalculationRepository
import com.example.common.models.Node
import com.example.network.IListenNodeMessagesUseCase
import com.example.network.models.NodeMessage
import com.example.nodes.data.repository.info.INodesInfoRepository

class HandleIncomingNodeMessagesUseCase(
    private val listenNodeMessagesUseCase: IListenNodeMessagesUseCase,
    private val calculationRepository: ICalculationRepository,
    private val nodesRepository: INodesInfoRepository,
) {

    suspend operator fun invoke() {
        listenNodeMessagesUseCase()
            .collect { (nodeId, message) ->
                when (message) {
                    is NodeMessage.Discovery -> {
                        nodesRepository.upsertNode(
                            node = Node(
                                id = nodeId,
                                name = message.name,
                                ip = message.ip,
                                port = message.port,
                            ),
                        )
                    }
                    is NodeMessage.StartedCalculation -> {
                        calculationRepository.markBatchInProgressIfWasFirst(
                            batch = message.batch,
                            nodeId = nodeId,
                            timestamp = message.timestamp,
                        )
                    }
                    is NodeMessage.EndedCalculation -> {
                        calculationRepository.markBatchChecked(batch = message.batch)
                    }
                    is NodeMessage.Health -> {
                        nodesRepository.updateHealth(
                            nodeId = nodeId,
                            timestamp = message.timestamp,
                        )
                    }
                }
            }
    }
}
