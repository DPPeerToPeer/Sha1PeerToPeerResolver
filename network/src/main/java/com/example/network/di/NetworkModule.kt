package com.example.network.di

import com.example.network.IDiscoveryUseCase
import com.example.network.IListenNodeMessagesUseCase
import com.example.network.IRunConnectionsHandlerUseCase
import com.example.network.ISendNodeMessageUseCase
import com.example.network.internal.data.nodes.ConnectionsHandler
import com.example.network.internal.data.nodes.IConnectionsHandler
import com.example.network.internal.useCase.DiscoveryUseCase
import com.example.network.internal.useCase.ListenNodeMessagesUseCase
import com.example.network.internal.useCase.RunConnectionsHandlerUseCase
import com.example.network.internal.useCase.SendNodeMessageUseCase
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
