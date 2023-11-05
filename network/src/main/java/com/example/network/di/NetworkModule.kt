package com.example.network.di

import com.example.network.IDiscoveryUseCase
import com.example.network.IListenNodeMessagesUseCase
import com.example.network.IRunConnectionsHandlerUseCase
import com.example.network.ISendNodeMessageUseCase
import com.example.network.internal.ConnectionsHandler
import com.example.network.internal.IConnectionsHandler
import com.example.network.internal.useCases.DiscoveryUseCase
import com.example.network.internal.useCases.ListenNodeMessagesUseCase
import com.example.network.internal.useCases.RunConnectionsHandlerUseCase
import com.example.network.internal.useCases.SendNodeMessageUseCase
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
    bindProvider<IListenNodeMessagesUseCase> {
        ListenNodeMessagesUseCase(instance())
    }
    bindProvider<ISendNodeMessageUseCase> {
        SendNodeMessageUseCase(instance())
    }
    bindProvider<IRunConnectionsHandlerUseCase> {
        RunConnectionsHandlerUseCase(instance())
    }
    bindProvider<IDiscoveryUseCase> {
        DiscoveryUseCase(instance())
    }
}
