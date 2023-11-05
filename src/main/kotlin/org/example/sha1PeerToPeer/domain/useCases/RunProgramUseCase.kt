package org.example.sha1PeerToPeer.domain.useCases

import kotlinx.coroutines.*
import org.example.sha1PeerToPeer.data.repository.calculation.ICalculationRepository
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class RunProgramUseCase(
    private val discoveryUseCase: DiscoveryUseCase,
    private val calculationRepository: ICalculationRepository,
    private val handleIncomingNodeMessagesUseCase: HandleIncomingNodeMessagesUseCase,
    private val runCalculationInBatchUseCase: MakeCalculationInBatchUseCase,
    private val sendHealthUseCase: SendHealthUseCase,
    private val removeNotActiveNodesUseCase: RemoveNotActiveNodesUseCase,
) {

    suspend operator fun invoke(
        hashToFind: String,
    ) = coroutineScope {
        discoveryUseCase.invoke(hashToFind = hashToFind)
        launch {
            handleIncomingNodeMessagesUseCase()
        }
        launch {
            while (isActive) {
                val batch = calculationRepository.getAvailableBatchAndMarkMine()
                batch?.let {
                    val job = launch {
                        runCalculationInBatchUseCase(batch = batch)
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
