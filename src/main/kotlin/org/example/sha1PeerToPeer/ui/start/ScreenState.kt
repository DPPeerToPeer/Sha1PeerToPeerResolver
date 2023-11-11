package org.example.sha1PeerToPeer.ui.start

import org.example.sha1PeerToPeer.domain.models.ProgramState

data class ScreenState(
    val programState: ProgramState,
    val hashToFind: String,
)
