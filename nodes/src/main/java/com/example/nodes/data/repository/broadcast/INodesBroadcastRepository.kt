package com.example.nodes.data.repository.broadcast

import com.example.common.models.Batch
import com.example.common.models.CalculationResult

/**
 * Each method send message to all nodes from NodesRepository
 */
interface INodesBroadcastRepository {


    //TODO
    suspend fun sendMyInfo(port: Int) // 1. Pobieram listę aktywnych nodów. 2. Dla każdego node zawołać metodę invoke z SendNodeMessageUseCase.
    //TODO
    suspend fun sendStart() // Będzie następnie tworzone

    suspend fun sendStartedCalculation (
        batch: Batch,
        timestamp: Long,
        //TODO
    )

    suspend fun sendEndedCalculation(
        batch: Batch,
        calculationResult: CalculationResult,
        //TODO
    )

    suspend fun sendHealth(
        timestamp: Long,
        //TODO
    )


}
