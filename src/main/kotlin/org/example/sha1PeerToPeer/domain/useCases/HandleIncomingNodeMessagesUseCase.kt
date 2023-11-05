package org.example.sha1PeerToPeer.domain.useCases

import org.example.sha1PeerToPeer.connections.IConnectionsHandler
import org.example.sha1PeerToPeer.connections.NodeMessage
import org.example.sha1PeerToPeer.data.repository.calculation.ICalculationRepository
import org.example.sha1PeerToPeer.data.repository.nodes.INodesRepository
import org.example.sha1PeerToPeer.domain.models.Node

class HandleIncomingNodeMessagesUseCase(
    private val connectionsHandler: IConnectionsHandler,
    private val calculationRepository: ICalculationRepository,
    private val nodesRepository: INodesRepository,
) {

    suspend operator fun invoke() {
        connectionsHandler.listenNodesMessages()
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
