package org.example.sha1PeerToPeer.domain.useCases

import com.example.calculation.ICalculationRepository
import com.example.calculation.domain.models.BatchState
import com.example.common.models.CalculationResult
import com.example.common.models.Node
import com.example.network.IGetIpOfNodeUseCase
import com.example.network.IListenNodeMessagesUseCase
import com.example.network.models.NodeMessage
import com.example.nodes.INodesBroadcastUseCase
import com.example.nodes.data.repository.info.INodesInfoRepository

internal class HandleIncomingNodeMessagesUseCase(
    private val listenNodeMessagesUseCase: IListenNodeMessagesUseCase,
    private val calculationRepository: ICalculationRepository,
    private val nodesRepository: INodesInfoRepository,
    private val getIpOfNodeUseCase: IGetIpOfNodeUseCase,
    private val resultFoundUseCase: ResultFoundUseCase,
    private val nodesBroadcastUseCase: INodesBroadcastUseCase,
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
                        val batchState = calculationRepository.getBatchState(batch = message.batch)
                        if (batchState is BatchState.Checked) {
                            nodesBroadcastUseCase.sendEndedCalculation(
                                batch = message.batch,
                                calculationResult = batchState.result,
                            )
                        } else if (batchState is BatchState.InProgressMine) {
                            nodesBroadcastUseCase.sendStartedCalculation(
                                batch = message.batch,
                                timestamp = batchState.startTimestamp,
                            )
                        }
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

                    NodeMessage.InitedConnection -> {
                        calculationRepository.getBatchMarkedMine()?.let { (batch, state) ->
                            nodesBroadcastUseCase.sendStartedCalculation(
                                batch = batch,
                                timestamp = state.startTimestamp,
                            )
                        }
                    }
                }
            }
    }
}
