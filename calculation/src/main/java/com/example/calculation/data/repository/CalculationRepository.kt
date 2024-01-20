package com.example.calculation.data.repository

import com.example.calculation.ICalculationRepository
import com.example.calculation.data.repository.db.ICalculationDao
import com.example.calculation.domain.models.BatchState
import com.example.calculation.domain.useCase.MakeCalculationInBatchUseCase
import com.example.common.IGetCurrentTimeUseCase
import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.common.models.NodeId

internal class CalculationRepository(
    private val dao: ICalculationDao,
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
    private val makeCalculationInBatchUseCase: MakeCalculationInBatchUseCase,
) : ICalculationRepository {

    override suspend fun startCalculation(
        batch: Batch,
        hashToFind: String,
    ): CalculationResult =
        makeCalculationInBatchUseCase(batch = batch, hashToFind = hashToFind)

    override suspend fun getAvailableBatchAndMarkMine(): Batch? =
        dao.getAvailableBatchAndMarkMine(timestamp = getCurrentTimeUseCase())

    override suspend fun isBatchTakenByOtherNodeOrChecked(batch: Batch): Boolean =
        dao.getBatchState(batch = batch).let {
            it is BatchState.InProgressOtherNode || it is BatchState.Checked
        }

    override suspend fun markBatchInProgressIfWasFirst(batch: Batch, nodeId: NodeId, timestamp: Long) {
        dao.markBatchInProgressIfWasFirst(
            batch = batch,
            nodeId = nodeId,
            timestamp = timestamp,
        )
    }

    override suspend fun markBatchChecked(batch: Batch) {
        dao.markBatchChecked(batch = batch)
    }

    override suspend fun markBatchesOfThisNodeAvailable(nodeId: NodeId) {
        dao.markBatchesOfThisNodeAvailable(nodeId = nodeId)
    }

    override suspend fun initialiseDB() {
        dao.clearDb()

//        listOf(
//            Batch(start = "a", end = "9"),
//            Batch(start = "aa", end = "99"),
//            Batch(start = "aaa", end = "999"),
//        ).forEach {
//            dao.insertBatch(batch = it)
//        }
    }
}
