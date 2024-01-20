package org.example.sha1PeerToPeer.domain.useCases

import com.example.calculation.ICalculationRepository
import com.example.common.models.CalculationResult
import com.example.common.models.Node
import com.example.network.IGetIpOfNodeUseCase
import com.example.network.IListenNodeMessagesUseCase
import com.example.network.models.NodeMessage
import com.example.nodes.data.repository.info.INodesInfoRepository

internal class HandleIncomingNodeMessagesUseCase(
    private val listenNodeMessagesUseCase: IListenNodeMessagesUseCase,
    private val calculationRepository: ICalculationRepository,
    private val nodesRepository: INodesInfoRepository,
    private val getIpOfNodeUseCase: IGetIpOfNodeUseCase,
    private val resultFoundUseCase: ResultFoundUseCase,
) {

    suspend operator fun invoke() {
        listenNodeMessagesUseCase()
            .collect { (nodeId, message) ->
                when (message) {
                    is NodeMessage.Discovery -> {
                        val ipOfNode = getIpOfNodeUseCase(nodeId = nodeId)
                        ipOfNode?.let {
                            nodesRepository.upsertNode(
                                node = Node(
                                    id = nodeId,
                                    name = message.name,
                                    ip = ipOfNode,
                                    port = message.port,
                                ),
                            )
                        }
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

                        message.calculationResult.let { result ->
                            if (result is CalculationResult.Found) {
                                resultFoundUseCase(
                                    batch = message.batch,
                                    solution = result.text,
                                )
                            }
                        }
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
