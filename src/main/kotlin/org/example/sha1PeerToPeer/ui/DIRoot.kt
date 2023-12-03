package org.example.sha1PeerToPeer.ui

import androidx.compose.runtime.Composable
import com.example.network.di.networkModule
import org.example.sha1PeerToPeer.di.appModule
import org.kodein.di.compose.withDI

@Composable
internal fun DIRoot(content: @Composable () -> Unit) {
    withDI(appModule, networkModule) {
        content()
    }
}
