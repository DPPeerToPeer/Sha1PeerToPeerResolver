package com.example.socketsFacade.di

import com.example.socketsFacade.IClientSocketFactory
import com.example.socketsFacade.IHttpDiscoveryClient
import com.example.socketsFacade.IServerSocketFactory
import com.example.socketsFacade.IUdpBroadcastSocket
import com.example.socketsFacade.internal.ClientSocketFactory
import com.example.socketsFacade.internal.HttpDiscoveryClient
import com.example.socketsFacade.internal.ServerSocketFactory
import com.example.socketsFacade.internal.UdpBroadcastSocket
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val socketsFacadeModule = DI.Module(name = "SocketsFacade") {
    bindProvider<IServerSocketFactory> {
        ServerSocketFactory()
    }
    bindSingleton<IUdpBroadcastSocket> {
        UdpBroadcastSocket()
    }
    bindProvider<IClientSocketFactory> {
        ClientSocketFactory()
    }
    bindSingleton<IHttpDiscoveryClient> {
        HttpDiscoveryClient()
    }
}
