package org.example.sha1PeerToPeer.connections

import org.example.sha1PeerToPeer.domain.models.Batch
import org.example.sha1PeerToPeer.domain.models.CalculationResult

sealed interface NodeMessage {

    data class Discovery(
        val ip: String,
        val port: Int,
        val name: String,
    ) : NodeMessage

    data class StartedCalculation(
        val batch: Batch,
        val timestamp: Long,
    ) : NodeMessage

    data class EndedCalculation(
        val batch: Batch,
        val calculationResult: CalculationResult,
    ) : NodeMessage

    data class Health(
        val timestamp: Long,
    ) : NodeMessage
}
