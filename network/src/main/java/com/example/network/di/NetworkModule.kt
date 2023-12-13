package com.example.network.di

import com.example.common.di.commonModule
import com.example.network.*
import com.example.network.internal.data.discovery.IDiscoveryApi
import com.example.network.internal.data.discovery.LocalhostDiscoveryApi
import com.example.network.internal.data.nodes.ConnectionsHandler
import com.example.network.internal.data.nodes.IConnectionsHandler
import com.example.network.internal.data.nodes.messagesProxy.IMessagesProxy
import com.example.network.internal.data.nodes.messagesProxy.MessagesProxy
import com.example.network.internal.data.nodes.myPort.IMyPortRepository
import com.example.network.internal.data.nodes.myPort.MyPortRepository
import com.example.network.internal.data.nodes.singleNodeConnection.factory.ISingleNodeConnectionFactory
import com.example.network.internal.data.nodes.singleNodeConnection.factory.SingleNodeConnectionFactory
import com.example.network.internal.data.nodes.singleNodeConnection.repository.SingleNodeConnectionRepository
import com.example.network.internal.useCase.*
import com.example.network.internal.useCase.DiscoveryUseCase
import com.example.network.internal.useCase.ListenNodeMessagesUseCase
import com.example.network.internal.useCase.RunConnectionsHandlerUseCase
import com.example.network.internal.useCase.SendNodeMessageUseCase
import com.example.socketsFacade.di.socketsFacadeModule
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

private val logger = KotlinLogging.logger {}

val networkModule = DI.Module(name = "Network") {
    import(socketsFacadeModule)
    import(commonModule)
    bindSingleton<IConnectionsHandler> {
        ConnectionsHandler(
            scope = instance(),
            serverSocketFactory = instance(),
            messagesProxy = instance(),
            myPortRepository = instance(),
            singleNodeConnectionRepository = instance(),
        )
    }
    bindSingleton<CoroutineScope> {
        CoroutineScope(
            SupervisorJob() + CoroutineExceptionHandler { coroutineContext, throwable ->
                logger.atError {
                    message = "Error in app scope children"
                    cause = throwable
                }
            },
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
        DiscoveryUseCase(
            discoveryApi = instance(),
            getMyIdUseCase = instance(),
        )
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
    bindSingleton<IMyPortRepository> {
        MyPortRepository()
    }
    bindSingleton {
        SingleNodeConnectionRepository(
            scope = instance(),
            singleNodeConnectionFactory = instance(),
            clientSocketFactory = instance(),
            myPortRepository = instance(),
            getMyIdUseCase = instance(),
        )
    }
}
