package org.example.sha1PeerToPeer

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.example.sha1PeerToPeer.ui.DIRoot
import org.example.sha1PeerToPeer.ui.NavigationRoot
import org.slf4j.simple.SimpleLogger

fun main() {
    System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE")
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Starting Page",
            state = WindowState(width = 400.dp, height = 400.dp),
        ) {
            MaterialTheme {
                DIRoot {
                    NavigationRoot()
                }
            }
        }
    }
}
