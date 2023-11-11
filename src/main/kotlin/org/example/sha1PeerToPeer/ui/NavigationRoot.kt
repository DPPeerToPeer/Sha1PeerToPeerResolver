package org.example.sha1PeerToPeer.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.example.sha1PeerToPeer.ui.start.StartScreen

@Composable
fun NavigationRoot() {
    Navigator(screen = StartScreen())
}
