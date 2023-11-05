package org.example.sha1PeerToPeer.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.example.sha1PeerToPeer.connections.ConnectionsHandler
import org.example.sha1PeerToPeer.connections.IConnectionsHandler
import org.example.sha1PeerToPeer.connections.ListenNodeMessagesUseCase
import org.example.sha1PeerToPeer.connections.SendNodeMessageUseCase
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val di = DI {
    bindSingleton<IConnectionsHandler> {
        ConnectionsHandler(
            scope = CoroutineScope(SupervisorJob()),
        )
    }
    bindProvider<ListenNodeMessagesUseCase> {
        ListenNodeMessagesUseCase(instance())
    }
    bindProvider<SendNodeMessageUseCase> {
        SendNodeMessageUseCase(instance())
    }
}
