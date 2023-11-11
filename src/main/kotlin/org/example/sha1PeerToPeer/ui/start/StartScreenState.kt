package org.example.sha1PeerToPeer.ui.start

data class StartScreenState( // ///klasa, ktora przechowuje dane
    val hashToFind: String,
    val isLoading: Boolean,
    val shouldNavigateToNextScreen: Boolean,
)
