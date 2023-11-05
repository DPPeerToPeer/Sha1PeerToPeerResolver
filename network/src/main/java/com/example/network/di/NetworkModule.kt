package com.example.network.di

import com.example.network.internal.ConnectionsHandler
import com.example.network.internal.IConnectionsHandler
import com.example.network.useCases.ListenNodeMessagesUseCase
import com.example.network.useCases.SendNodeMessageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val networkModule = DI.Module(name = "Network") {
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
