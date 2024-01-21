package com.example.calculation

import com.example.calculation.domain.models.BatchState
import com.example.calculation.domain.models.CalculationStatistics
import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.common.models.NodeId
import kotlinx.coroutines.flow.Flow

interface ICalculationRepository {

    /**
     * Start calculation on given batch and mark as InProgressMine if not already taken with previous timestamp.
     */
    suspend fun startCalculation(
        batch: Batch,
        hashToFind: String,
    ): CalculationResult

    /**
     * Get available batch and mark BatchState as InProgressMine
     */
    suspend fun getAvailableBatchAndMarkMine(): Batch?

    /**
     * Return true if BatchState of given batch is InProgressOtherNode or Checked
     */
    suspend fun isBatchTakenByOtherNodeOrChecked(batch: Batch): Boolean

    /**
     * Mark BatchState of batch as InProgressOtherNode if it's not already Checked.
     * If it's already InProgress than compare timestamps and put nodeId with lower timestamp
     */
    suspend fun markBatchInProgressIfWasFirst(
        batch: Batch,
        nodeId: NodeId,
        timestamp: Long,
    )

    /**
     * Mark BatchState of batch as checked
     */
    suspend fun markBatchChecked(batch: Batch)

    /**
     * Mark batches which are in state InProgressOtherNode with nodeId equal nodeId as Available
     */
    suspend fun markBatchesOfThisNodeAvailable(nodeId: NodeId)

    fun observeStatistics(): Flow<CalculationStatistics>

    suspend fun initialiseDB()

    suspend fun getBatchState(batch: Batch): BatchState
}
