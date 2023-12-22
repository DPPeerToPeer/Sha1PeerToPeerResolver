package org.example.sha1PeerToPeer.domain.useCases

import com.example.calculation.ICalculationRepository
import com.example.common.*
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
                            calculationRepository.startCalculation(batch = batch)
                        }
                        while (!job.isCompleted) {
                            ensureActive()
                            if (calculationRepository.isBatchTakenByOtherNode(batch = batch)) {
                                job.cancel()
                                break
                            }
                            delay(CHECK_IS_BATCH_TAKEN_INTERVAL)
                        }
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
                        calculationRepository.marchBatchesOfThisNodeAvailable(nodeId = it.id)
                    }
                    delay(REMOVE_NODE_AFTER_INACTIVITY_DURATION)
                }
            }
        }
    }
}
