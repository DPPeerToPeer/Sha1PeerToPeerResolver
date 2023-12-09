package org.example.sha1PeerToPeer.domain.useCases.runProgram

import com.example.calculation.ICalculationRepository
import com.example.common.*
import com.example.network.IDiscoveryUseCase
import com.example.network.IRunConnectionsHandlerUseCase
import com.example.network.models.Port
import com.example.nodes.data.repository.broadcast.INodesBroadcastRepository
import com.example.nodes.data.repository.info.INodesInfoRepository
import com.example.nodes.domain.useCase.RemoveNotActiveNodesUseCase
import com.example.nodes.domain.useCase.SendHealthUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.sha1PeerToPeer.domain.useCases.HandleIncomingNodeMessagesUseCase

internal class RunProgramUseCase(
    private val runConnectionsHandlerUseCase: IRunConnectionsHandlerUseCase,
    private val discoveryUseCase: IDiscoveryUseCase,
    private val calculationRepository: ICalculationRepository,
    private val handleIncomingNodeMessagesUseCase: HandleIncomingNodeMessagesUseCase,
    private val sendHealthUseCase: SendHealthUseCase,
    private val removeNotActiveNodesUseCase: RemoveNotActiveNodesUseCase,
    private val nodesRepository: INodesInfoRepository,
    private val nodesBroadcastRepository: INodesBroadcastRepository,
    private val getMyIdUseCase: IGetMyIdUseCase,
    private val appScope: CoroutineScope,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
) : IRunProgramUseCase {

    private var job: Job? = null
    private val mutex: Mutex = Mutex()

    override suspend fun invoke(
        hashToFind: String,
    ) {
        mutex.withLock {
            if (job == null) {
                val myPort = runConnectionsHandlerUseCase.invoke()

                job = appScope.launch {
                    makeRepetitiveOperations(
                        hashToFind = hashToFind,
                        myPort = myPort,
                    )
                }
            }
        }
    }

    private suspend fun makeRepetitiveOperations(
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
                            nodesBroadcastRepository.sendStartedCalculation(batch = batch, timestamp = getCurrentTimeUseCase())
                            calculationRepository.startCalculation(batch = batch)
                        }
                        while (!job.isCompleted) {
                            ensureActive()
                            if (calculationRepository.isBatchTakenByOtherNode(batch = batch)) {
                                job.cancel()
                                break
                            }
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
                    removeNotActiveNodesUseCase()
                    delay(REMOVE_NODE_AFTER_INACTIVITY_DURATION)
                }
            }
        }
    }
}
