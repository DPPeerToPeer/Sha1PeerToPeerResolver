package org.example.sha1PeerToPeer.domain.useCases

import org.example.sha1PeerToPeer.connections.IConnectionsHandler
import org.example.sha1PeerToPeer.connections.NodeMessage
import org.example.sha1PeerToPeer.data.repository.calculation.ICalculationRepository
import org.example.sha1PeerToPeer.data.repository.nodes.INodesRepository

class HandleIncomingNodeMessagesUseCase constructor(
    private val connectionsHandler: IConnectionsHandler,
    private val calculationRepository: ICalculationRepository,
    private val nodesRepository: INodesRepository,
) {

    suspend operator fun invoke() {
        connectionsHandler.listenNodesMessages()
            .collect { (node, message) ->
                when (message) {
                    is NodeMessage.Discovery -> {
                        nodesRepository.addNewNode(node = node)
                    }
                    is NodeMessage.StartedCalculation -> {
                        calculationRepository.markBatchInProgressAndAdjust(
                            batch = message.batch,
                            node = node,
                            timestamp = message.timestamp,
                        )
                    }
                    is NodeMessage.EndedCalculation -> {
                        calculationRepository.markBatchChecked(batch = message.batch)
                    }
                    is NodeMessage.Health -> {
                        nodesRepository.updateHealth(
                            node = node,
                            timestamp = message.timestamp,
                        )
                    }
                }
            }
    }
}
