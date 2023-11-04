package org.example.sha1PeerToPeer.domain.models

sealed interface CalculationResult {
    object NotFound : CalculationResult

    data class Found(val text: String) : CalculationResult
}
