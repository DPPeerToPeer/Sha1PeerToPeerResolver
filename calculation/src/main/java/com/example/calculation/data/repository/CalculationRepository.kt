package com.example.calculation.data.repository

import com.example.calculation.ICalculationRepository
import com.example.calculation.domain.models.BatchState
import com.example.common.models.Batch
import com.example.common.models.NodeId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex

internal class CalculationRepository() : ICalculationRepository {

    override val batches: StateFlow<Map<Batch, BatchState>>
        get() = _batches.asStateFlow()

    private val _batches = MutableStateFlow<Map<Batch, BatchState>>(emptyMap())

    private val mutex = Mutex()

    override suspend fun startCalculation(batch: Batch) {
        TODO("Not yet implemented")
    }

    override suspend fun getAvailableBatchAndMarkMine(): Batch? {
        TODO("Not yet implemented")
    }

    override suspend fun isBatchTakenByOtherNode(batch: Batch): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun markBatchInProgressIfWasFirst(batch: Batch, nodeId: NodeId, timestamp: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun markBatchChecked(batch: Batch) {
        TODO("Not yet implemented")
    }

    override suspend fun marchBatchesOfThisNodeAvailable(nodeId: NodeId) {
        TODO("Not yet implemented")
    }
}
