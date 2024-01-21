package org.example.sha1PeerToPeer.ui.calculation

import com.example.calculation.domain.models.CalculationStatistics
import com.example.common.models.Node

sealed interface CalculationState {
    object Loading : CalculationState

    data class Calculation(
        val statistics: CalculationStatistics,
        val nodes: List<Node> = emptyList(),
    ) : CalculationState

    data class Found(
        val result: String,
    ) : CalculationState
}
