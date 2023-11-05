package org.example.sha1PeerToPeer.domain.useCases

import com.example.calculation.ICalculationRepository
import com.example.nodes.domain.useCase.RemoveNotActiveNodesUseCase
import com.example.nodes.domain.useCase.SendHealthUseCase
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class RunProgramUseCase(
    private val discoveryUseCase: DiscoveryUseCase,
    private val calculationRepository: ICalculationRepository,
    private val handleIncomingNodeMessagesUseCase: HandleIncomingNodeMessagesUseCase,
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
