package com.example.nodes.data.repository.broadcast

import com.example.common.models.Batch
import com.example.common.models.CalculationResult

/**
 * Each method send message to all nodes from NodesRepository
 */
interface INodesBroadcastRepository {

    suspend fun sendMyInfo(port: Int)

    // TODO
    suspend fun sendStart() // Będzie następnie tworzone //TODO

    // Invokes method from sendNodeMessageUseCase on every node sending NodeMessage.StartedCalculation message
    suspend fun sendStartedCalculation(
        batch: Batch,
        timestamp: Long,
    )

    // Invokes method from sendNodeMessageUseCase on every node sending NodeMessage.EndedCalculation message
    suspend fun sendEndedCalculation(
        batch: Batch,
        calculationResult: CalculationResult,
    )

    // Invokes method from sendNodeMessageUseCase on every node sending NodeMessage.Health message
    suspend fun sendHealth(
        timestamp: Long,
    )
}
