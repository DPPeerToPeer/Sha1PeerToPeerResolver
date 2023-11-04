package org.example.sha1PeerToPeer.domain.models

sealed interface BatchState {

    object Empty : BatchState

    data class InProgress(val node: Node) : BatchState

    object Checked : BatchState
}
