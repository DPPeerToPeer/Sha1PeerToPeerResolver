package com.example.network

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import kotlinx.serialization.Serializable

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