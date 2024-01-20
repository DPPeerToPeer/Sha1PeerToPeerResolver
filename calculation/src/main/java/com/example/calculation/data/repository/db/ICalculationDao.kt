package com.example.calculation.data.repository.db

import com.example.calculation.domain.models.BatchState
import com.example.common.models.Batch
import com.example.common.models.NodeId

internal interface ICalculationDao {
    suspend fun getBatchState(batch: Batch): BatchState

    suspend fun getAvailableBatchAndMarkMine(timestamp: Long): Batch?

    suspend fun markBatchInProgressIfWasFirst(batch: Batch, nodeId: NodeId, timestamp: Long)

    suspend fun markBatchChecked(batch: Batch)

    suspend fun markBatchesOfThisNodeAvailable(nodeId: NodeId)

    suspend fun insertBatch(batch: Batch)

    suspend fun clearDb()
}
