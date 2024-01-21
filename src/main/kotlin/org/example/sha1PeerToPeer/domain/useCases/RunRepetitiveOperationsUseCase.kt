package org.example.sha1PeerToPeer.domain.useCases

import com.example.calculation.ICalculationRepository
import com.example.common.*
import com.example.common.models.CalculationResult
import com.example.network.IDiscoveryUseCase
import com.example.network.models.Port
import com.example.nodes.INodesBroadcastUseCase
import com.example.nodes.IRemoveNotActiveNodesUseCase
import com.example.nodes.ISendHealthUseCase
import com.example.nodes.data.repository.info.INodesInfoRepository
import kotlinx.coroutines.*

internal class RunRepetitiveOperationsUseCase(
    private val discoveryUseCase: IDiscoveryUseCase,
    private val calculationRepository: ICalculationRepository,
    private val handleIncomingNodeMessagesUseCase: HandleIncomingNodeMessagesUseCase,
    private val sendHealthUseCase: ISendHealthUseCase,
    private val removeNotActiveNodesUseCase: IRemoveNotActiveNodesUseCase,
    private val nodesRepository: INodesInfoRepository,
    private val nodesBroadcastUseCase: INodesBroadcastUseCase,
    private val getMyIdUseCase: IGetMyIdUseCase,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
    private val resultsFoundUseCase: ResultFoundUseCase,
    private val appScope: CoroutineScope,
) {

    suspend operator fun invoke(
        hashToFind: String,
        myPort: Port,
    ) {
        coroutineScope {
            launch {
                discoveryUseCase.invoke(
                    hashToFind = hashToFind,
                    myPort = myPort.port,
                    myId = getMyIdUseCase(),
                    myName = "Name",
                ).collect {
                    nodesRepository.upsertNode(node = it)
                }
            }

            delay(AFTER_DISCOVERY_PROGRAM_DELAY) // Give time for discovery for some nodes and start

            launch {
                handleIncomingNodeMessagesUseCase()
            }
            launch {
                while (isActive) {
                    val batch = calculationRepository.getAvailableBatchAndMarkMine()
                    batch?.let {
                        val job = launch {
                            launch {
                                nodesBroadcastUseCase.sendStartedCalculation(batch = batch, timestamp = getCurrentTimeUseCase())
                            }
                            val calculationResult = calculationRepository
                                .startCalculation(batch = batch, hashToFind = hashToFind)

                            when (calculationResult) {
                                is CalculationResult.NotFound -> {
                                    appScope.launch {
                                        nodesBroadcastUseCase.sendEndedCalculation(
                                            batch = batch,
                                            calculationResult = calculationResult,
                                        )
                                    }
                                    calculationRepository.markBatchChecked(batch = batch)
                                }
                                is CalculationResult.Found -> {
                                    resultsFoundUseCase(solution = calculationResult.text, batch = batch)
                                }
                            }
                        }

                        calculationRepository.awaitBatchTakenByOthers(batch = batch)
                        println("Cancelling job with batch $batch")
                        job.cancel()
                    } ?: delay(TRY_AGAIN_DELAY_IF_NO_BATCH_AVAILABLE)
                }
            }
            launch {
                while (isActive) {
                    sendHealthUseCase()
                    delay(SEND_HEALTH_INTERVAL)
                }
            }
            launch {
                while (isActive) {
                    val removedNodes = removeNotActiveNodesUseCase()
                    removedNodes.forEach {
                        calculationRepository.markBatchesOfThisNodeAvailable(nodeId = it.id)
                    }
                    delay(REMOVE_NODE_AFTER_INACTIVITY_DURATION)
                }
            }
        }
    }
}
