package com.example.common.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface CalculationResult {
    @Serializable
    object NotFound : CalculationResult

    @Serializable
    data class Found(val text: String) : CalculationResult
}
