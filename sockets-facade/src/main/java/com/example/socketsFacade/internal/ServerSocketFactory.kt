package com.example.socketsFacade.internal

import com.example.socketsFacade.IServerSocket
import com.example.socketsFacade.IServerSocketFactory
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers

internal class ServerSocketFactory : IServerSocketFactory {

    override fun create(): IServerSocket {
        val selectorManager = SelectorManager(dispatcher = Dispatchers.IO)
        return aSocket(selectorManager).tcp().bind().let(::ServerSocketImpl)
    }
}
