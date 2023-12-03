package org.example.sha1PeerToPeer.domain.useCases.runProgram

import com.example.calculation.ICalculationRepository
import com.example.network.IDiscoveryUseCase
import com.example.network.IRunConnectionsHandlerUseCase
import com.example.nodes.data.repository.broadcast.INodesBroadcastRepository
import com.example.nodes.data.repository.info.INodesInfoRepository
import com.example.nodes.domain.useCase.RemoveNotActiveNodesUseCase
import com.example.nodes.domain.useCase.SendHealthUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.sha1PeerToPeer.domain.models.ProgramState
import org.example.sha1PeerToPeer.domain.useCases.HandleIncomingNodeMessagesUseCase
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal class RunProgramUseCase(
    private val runConnectionsHandlerUseCase: IRunConnectionsHandlerUseCase,
    private val discoveryUseCase: IDiscoveryUseCase,
    private val calculationRepository: ICalculationRepository,
    private val handleIncomingNodeMessagesUseCase: HandleIncomingNodeMessagesUseCase,
    private val sendHealthUseCase: SendHealthUseCase,
    private val removeNotActiveNodesUseCase: RemoveNotActiveNodesUseCase,
    private val nodesRepository: INodesInfoRepository,
    private val nodesBroadcastRepository: INodesBroadcastRepository,
    private val appScope: CoroutineScope,
) : IRunProgramUseCase {

    private var job: Job? = null
    private val mutex: Mutex = Mutex()

    private val programStateFlow: MutableStateFlow<ProgramState> = MutableStateFlow(ProgramState.NOT_STARTED)

    override suspend fun invoke(hashToFind: String) {
        mutex.withLock {
            if (job == null) {
                programStateFlow.value = ProgramState.INITIALIZING
                val myPort = runConnectionsHandlerUseCase.invoke()
                val currentNodes = discoveryUseCase.invoke(
                    hashToFind = hashToFind,
                    myPort = myPort.port,
                )
                nodesRepository.upsertManyNodes(nodes = currentNodes)

                nodesBroadcastRepository.sendMyInfo(
                    port = myPort.port,
                )

                job = appScope.launch {
                    makeRepetitiveOperations()
                }
            }
        }
    }

    private suspend fun makeRepetitiveOperations() {
        coroutineScope {
            programStateFlow.value = ProgramState.RUNNING

            launch {
                handleIncomingNodeMessagesUseCase()
            }
            launch {
                while (isActive) {
                    val batch = calculationRepository.getAvailableBatchAndMarkMine()
                    batch?.let {
                        val job = launch {
                            nodesBroadcastRepository.sendStartedCalculation(batch = batch, timestamp = System.currentTimeMillis())
                            calculationRepository.startCalculation(batch = batch)
                        }
                        while (!job.isCompleted) {
                            ensureActive()
                            if (calculationRepository.isBatchTakenByOtherNode(batch = batch)) {
                                job.cancel()
                                break
                            }
                        }
                    } ?: delay(1.seconds)
                }
            }
            launch {
                while (isActive) {
                    sendHealthUseCase()
                    delay(10.seconds)
                }
            }
            launch {
                while (isActive) {
                    removeNotActiveNodesUseCase()
                    delay(1.minutes)
                }
            }
        }
    }
}
