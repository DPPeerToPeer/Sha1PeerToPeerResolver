package com.example.network.di

import com.example.network.*
import com.example.network.internal.data.discovery.IDiscoveryApi
import com.example.network.internal.data.discovery.LocalhostDiscoveryApi
import com.example.network.internal.data.nodes.ConnectionsHandler
import com.example.network.internal.data.nodes.IConnectionsHandler
import com.example.network.internal.data.nodes.messagesProxy.IMessagesProxy
import com.example.network.internal.data.nodes.messagesProxy.MessagesProxy
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionFactory
import com.example.network.internal.data.nodes.singleNodeConnection.SingleNodeConnectionFactory
import com.example.network.internal.useCase.*
import com.example.network.internal.useCase.DiscoveryUseCase
import com.example.network.internal.useCase.ListenNodeMessagesUseCase
import com.example.network.internal.useCase.RunConnectionsHandlerUseCase
import com.example.network.internal.useCase.SendNodeMessageUseCase
import com.example.socketsFacade.di.socketsFacadeModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val networkModule = DI.Module(name = "Network") {
    import(socketsFacadeModule)
    bindSingleton<IConnectionsHandler> {
        ConnectionsHandler(
            scope = CoroutineScope(SupervisorJob()),
            serverSocketFactory = instance(),
            singleNodeConnectionFactory = instance(),
            messagesProxy = instance(),
        )
    }
    bindProvider<ISingleNodeConnectionFactory> {
        SingleNodeConnectionFactory(messagesProxy = instance())
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
    bindSingleton<IMessagesProxy> {
        MessagesProxy()
    }
    bindProvider<IDiscoveryApi> {
        LocalhostDiscoveryApi(udpBroadcastSocket = instance())
    }
    bindProvider<IGetIpOfNodeUseCase> {
        GetIpOfNodeUseCase(connectionHandler = instance())
    }
}
