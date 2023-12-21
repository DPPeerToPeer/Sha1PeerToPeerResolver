package com.example.nodes

import com.example.common.models.Batch
import com.example.common.models.CalculationResult

/**
 * Each method send message to all nodes from NodesRepository
 */
interface INodesBroadcastUseCase {

    suspend fun sendStartedCalculation(
        batch: Batch,
        timestamp: Long,
    )

    suspend fun sendEndedCalculation(
        batch: Batch,
        calculationResult: CalculationResult,
    )

    suspend fun sendHealth(
        timestamp: Long,
    )
}
