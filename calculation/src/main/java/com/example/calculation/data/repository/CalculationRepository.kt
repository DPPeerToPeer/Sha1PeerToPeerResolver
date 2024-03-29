package com.example.calculation.data.repository

import com.example.calculation.ICalculationRepository
import com.example.calculation.data.repository.db.ICalculationDao
import com.example.calculation.domain.models.BatchState
import com.example.calculation.domain.models.CalculationStatistics
import com.example.calculation.domain.useCase.MakeCalculationInBatchUseCase
import com.example.common.IGetCurrentTimeUseCase
import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.common.models.NodeId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class CalculationRepository(
    private val dao: ICalculationDao,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
    private val makeCalculationInBatchUseCase: MakeCalculationInBatchUseCase,
) : ICalculationRepository {

    private val mutex by lazy {
        Mutex()
    }

    override suspend fun startCalculation(
        batch: Batch,
        hashToFind: String,
    ): CalculationResult =
        makeCalculationInBatchUseCase(batch = batch, hashToFind = hashToFind)

    override suspend fun getAvailableBatchAndMarkMine(): Batch? =
        mutex.withLock {
            dao.getAvailableBatchAndMarkMine(timestamp = getCurrentTimeUseCase())
        }

    override suspend fun getBatchState(batch: Batch): BatchState =
        mutex.withLock {
            dao.getBatchState(batch = batch)
        }

    override suspend fun awaitBatchTakenByOthers(batch: Batch) {
        dao.observeBatchState(batch = batch).first { it is BatchState.InProgressOtherNode || it is BatchState.Checked }
    }

    override suspend fun getBatchMarkedMine(): Pair<Batch, BatchState.InProgressMine>? =
        mutex.withLock {
            dao.getBatchMarkedMine()
        }

    override suspend fun markBatchInProgressIfWasFirst(batch: Batch, nodeId: NodeId, timestamp: Long) {
        mutex.withLock {
            dao.markBatchInProgressIfWasFirst(
                batch = batch,
                nodeId = nodeId,
                timestamp = timestamp,
            )
        }
    }

    override suspend fun markBatchChecked(batch: Batch) {
        mutex.withLock {
            dao.markBatchChecked(batch = batch)
        }
    }

    override suspend fun markBatchesOfThisNodeAvailable(nodeId: NodeId) {
        mutex.withLock {
            dao.markBatchesOfThisNodeAvailable(nodeId = nodeId)
        }
    }

    override fun observeStatistics(): Flow<CalculationStatistics> =
        dao.observeStatistics()

    override suspend fun initialiseDB() {
        dao.clearDb()
    }
}
