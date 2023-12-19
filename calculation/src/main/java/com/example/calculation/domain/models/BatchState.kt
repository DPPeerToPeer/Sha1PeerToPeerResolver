package com.example.calculation.domain.models

import com.example.common.models.CalculationResult
import com.example.common.models.NodeId

sealed interface BatchState {

    object Available : BatchState

    data class InProgressOtherNode(
        val nodeId: NodeId,
        val startTimestamp: Long,
    ) : BatchState

    data class InProgressMine(val startTimestamp: Long) : BatchState

    data class Checked(
        val result: CalculationResult,
    ) : BatchState
}
