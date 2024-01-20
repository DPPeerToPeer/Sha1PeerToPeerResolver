package org.example.sha1PeerToPeer.domain.useCases.runProgram

import com.example.calculation.ICalculationRepository
import com.example.common.ISyncTimeUseCase
import com.example.network.IRunConnectionsHandlerUseCase
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.sha1PeerToPeer.domain.useCases.RunRepetitiveOperationsUseCase

private val logger = KotlinLogging.logger {}

internal class RunProgramUseCase(
    private val runConnectionsHandlerUseCase: IRunConnectionsHandlerUseCase,
    private val appScope: CoroutineScope,
    private val runRepetitiveOperationsUseCase: RunRepetitiveOperationsUseCase,
    private val syncTimeUseCase: ISyncTimeUseCase,
    private val calculationRepository: ICalculationRepository,
) : IRunProgramUseCase {

    private var job: Job? = null
    private val mutex: Mutex = Mutex()

    override suspend fun invoke(
        hashToFind: String,
    ) {
        logger.atDebug {
            message = "invoke"
            payload = buildMap {
                put("hashToFind", hashToFind)
            }
        }
        mutex.withLock {
            if (job == null) {
                calculationRepository.initialiseDB()
                syncTimeUseCase()
                val myPort = runConnectionsHandlerUseCase.invoke()

                job = appScope.launch {
                    runRepetitiveOperationsUseCase(
                        hashToFind = hashToFind,
                        myPort = myPort,
                    )
                }
            }
        }
    }

    override fun cancelOperations() {
        job?.cancelChildren()
    }
}
