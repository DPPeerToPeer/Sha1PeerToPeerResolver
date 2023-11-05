package org.example.sha1PeerToPeer.domain.models

sealed interface BatchState {

    object Available : BatchState

    data class InProgressOtherNode(
        val nodeId: SocketId,
        val startTimestamp: Long,
    ) : BatchState

    data class InProgressMine(val startTimestamp: Long) : BatchState

    object Checked : BatchState
}
