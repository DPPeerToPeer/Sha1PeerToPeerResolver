package org.example.sha1PeerToPeer.connections

import kotlinx.serialization.Serializable
import org.example.sha1PeerToPeer.domain.models.Batch
import org.example.sha1PeerToPeer.domain.models.CalculationResult

@Serializable
sealed interface NodeMessage {

    @Serializable
    data class Discovery(
        val ip: String,
        val port: Int,
        val name: String,
    ) : NodeMessage

    @Serializable
    data class StartedCalculation(
        val batch: Batch,
        val timestamp: Long,
    ) : NodeMessage

    @Serializable
    data class EndedCalculation(
        val batch: Batch,
        val calculationResult: CalculationResult,
    ) : NodeMessage

    @Serializable
    data class Health(
        val timestamp: Long,
    ) : NodeMessage
}
