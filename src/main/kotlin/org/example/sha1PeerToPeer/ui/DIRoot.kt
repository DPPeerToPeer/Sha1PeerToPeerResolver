package org.example.sha1PeerToPeer.ui

import androidx.compose.runtime.Composable
import org.example.sha1PeerToPeer.di.appModule
import org.kodein.di.compose.withDI

@Composable
internal fun DIRoot(content: @Composable () -> Unit) {
    withDI(appModule) {
        content()
    }
}
