package com.example.nodes.data.repository.broadcast

import com.example.common.models.Batch
import com.example.common.models.CalculationResult

/**
 * Each method send message to all nodes from NodesRepository
 */
interface INodesBroadcastRepository {

    suspend fun sendMyInfo(
        port: Int,
        ip: String,
    )

    suspend fun sendStart()

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
