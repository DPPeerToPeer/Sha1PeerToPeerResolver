package com.example.calculation

import com.example.calculation.domain.models.BatchState
import com.example.common.models.Batch
import com.example.common.models.SocketId
import kotlinx.coroutines.flow.StateFlow

interface ICalculationRepository {

    val batches: StateFlow<Map<Batch, BatchState>>

    /**
     * Send message to other nodes via INodesApi and start calculation.
     */
    suspend fun startCalculation(batch: Batch)

    /**
     * Get available batch and mark BatchState as InProgressMine
     */
    suspend fun getAvailableBatchAndMarkMine(): Batch?

    /**
     * Return true if BatchState of given batch is InProgressOtherNode or Checked
     */
    suspend fun isBatchTakenByOtherNode(batch: Batch): Boolean

    /**
     * Mark BatchState of batch as InProgressOtherNode if it's not already Checked.
     * If it's already InProgress than compare timestamps and put nodeId with lower timestamp
     */
    suspend fun markBatchInProgressIfWasFirst(
        batch: Batch,
        nodeId: SocketId,
        timestamp: Long,
    )

    /**
     * Mark BatchState of batch as checked
     */
    suspend fun markBatchChecked(batch: Batch)
}
