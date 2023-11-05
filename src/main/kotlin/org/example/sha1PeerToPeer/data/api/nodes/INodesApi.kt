package org.example.sha1PeerToPeer.data.api.nodes

import org.example.sha1PeerToPeer.domain.models.Batch
import org.example.sha1PeerToPeer.domain.models.CalculationResult

/**
 * Each method send message to all nodes from NodesRepository
 */
interface INodesApi {

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
