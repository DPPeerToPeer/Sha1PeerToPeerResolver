package org.example.sha1PeerToPeer.domain.useCases

import com.example.network.IListenNodeMessagesUseCase
import com.example.network.models.NodeMessage
import com.example.nodes.data.repository.INodesRepository
import com.example.nodes.domain.models.Node
import org.example.sha1PeerToPeer.data.repository.calculation.ICalculationRepository

class HandleIncomingNodeMessagesUseCase(
    private val listenNodeMessagesUseCase: IListenNodeMessagesUseCase,
    private val calculationRepository: ICalculationRepository,
    private val nodesRepository: INodesRepository,
) {

    suspend operator fun invoke() {
        listenNodeMessagesUseCase()
            .collect { (socketId, message) ->
                when (message) {
                    is NodeMessage.Discovery -> {
                        nodesRepository.upsertNode(
                            node = Node.DiscoveredNode(
                                socketId = socketId,
                                name = message.name,
                                ip = message.ip,
                                port = message.port,
                            ),
                        )
                    }
                    is NodeMessage.StartedCalculation -> {
                        calculationRepository.markBatchInProgressIfWasFirst(
                            batch = message.batch,
                            nodeId = socketId,
                            timestamp = message.timestamp,
                        )
                    }
                    is NodeMessage.EndedCalculation -> {
                        calculationRepository.markBatchChecked(batch = message.batch)
                    }
                    is NodeMessage.Health -> {
                        nodesRepository.updateHealth(
                            socketId = socketId,
                            timestamp = message.timestamp,
                        )
                    }
                }
            }
    }
}
