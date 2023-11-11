package org.example.sha1PeerToPeer.domain.useCases

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
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class RunProgramUseCase(
    private val runConnectionsHandlerUseCase: IRunConnectionsHandlerUseCase,
    private val discoveryUseCase: IDiscoveryUseCase,
    private val calculationRepository: ICalculationRepository,
    private val handleIncomingNodeMessagesUseCase: HandleIncomingNodeMessagesUseCase,
    private val sendHealthUseCase: SendHealthUseCase,
    private val removeNotActiveNodesUseCase: RemoveNotActiveNodesUseCase,
    private val nodesRepository: INodesInfoRepository,
    private val nodesBroadcastRepository: INodesBroadcastRepository,
    private val appScope: CoroutineScope,
) {

    private var job: Job? = null
    private val mutex: Mutex = Mutex()

    private val programStateFlow: MutableStateFlow<ProgramState> = MutableStateFlow(ProgramState.NOT_STARTED)

    fun runAndObserveProgramState(hashToFind: String): Flow<ProgramState> = flow {
        mutex.withLock {
            if (job == null) {
                job = appScope.launch {
                    start(hashToFind = hashToFind)
                }
            }
        }

        emitAll(programStateFlow)
    }

    private suspend fun start(
        hashToFind: String,
    ) {
        coroutineScope {
            programStateFlow.value = ProgramState.INITIALIZING
            val myAddress = runConnectionsHandlerUseCase.invoke()
            val currentNodes = discoveryUseCase.invoke(
                hashToFind = hashToFind,
                myPort = myAddress.port,
            )
            nodesRepository.upsertManyNodes(nodes = currentNodes)

            nodesBroadcastRepository.sendMyInfo(
                port = myAddress.port,
            )

            programStateFlow.value = ProgramState.RUNNING

            launch {
                handleIncomingNodeMessagesUseCase()
            }
            launch {
                while (isActive) {
                    val batch = calculationRepository.getAvailableBatchAndMarkMine()
                    batch?.let {
                        val job = launch {
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
