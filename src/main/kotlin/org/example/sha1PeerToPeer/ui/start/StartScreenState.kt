package org.example.sha1PeerToPeer.ui.start

data class StartScreenState(
    val hashToFind: String,
    val isLoading: Boolean,
    val shouldNavigateToNextScreen: Boolean,
)
