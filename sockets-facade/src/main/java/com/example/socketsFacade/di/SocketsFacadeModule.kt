package com.example.socketsFacade.di

import com.example.socketsFacade.IServerSocketFactory
import com.example.socketsFacade.IUdpBroadcastSocket
import com.example.socketsFacade.internal.ServerSocketFactory
import com.example.socketsFacade.internal.UdpBroadcastSocket
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val socketsFacadeModule = DI.Module(name = "SocketsFacade") {
    bindSingleton<IServerSocketFactory> {
        ServerSocketFactory()
    }
    bindSingleton<IUdpBroadcastSocket> {
        UdpBroadcastSocket()
    }
}
