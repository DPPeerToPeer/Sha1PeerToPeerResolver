package com.example.calculation.domain.models

import com.example.common.models.SocketId

sealed interface BatchState {

    object Available : BatchState

    data class InProgressOtherNode(
        val nodeId: SocketId,
        val startTimestamp: Long,
    ) : BatchState

    data class InProgressMine(val startTimestamp: Long) : BatchState

    object Checked : BatchState
}
