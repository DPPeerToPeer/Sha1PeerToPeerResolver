package org.example.sha1PeerToPeer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.example.sha1PeerToPeer.ui.DIRoot
import org.example.sha1PeerToPeer.ui.NavigationRoot

@Composable
@Preview
fun FirstPage() {
    val viewModel = remember {
        AppViewModel()
    }
}
fun main() = application {
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
