package org.example.sha1PeerToPeer.data.repository.calculation

import kotlinx.coroutines.flow.StateFlow
import org.example.sha1PeerToPeer.domain.models.Batch
import org.example.sha1PeerToPeer.domain.models.BatchState
import org.example.sha1PeerToPeer.domain.models.Node

interface ICalculationRepository {

    val batches: StateFlow<List<Pair<Batch, BatchState>>>

    suspend fun startCalculation(batch: Batch)

    suspend fun markBatchInProgressAndAdjust(
        batch: Batch,
        node: Node,
        timestamp: Long,
    )

    suspend fun markBatchChecked(batch: Batch)
}
