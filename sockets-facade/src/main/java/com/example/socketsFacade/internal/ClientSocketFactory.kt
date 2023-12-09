package com.example.socketsFacade.internal

import com.example.socketsFacade.IClientSocketFactory
import com.example.socketsFacade.IReadWriteSocket
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ClientSocketFactory : IClientSocketFactory {

    override suspend fun create(ip: String, port: Int): IReadWriteSocket {
        val selectorManager = SelectorManager(Dispatchers.IO)
        return withContext(Dispatchers.IO) {
            aSocket(selectorManager).tcp().connect(InetSocketAddress(ip, port)).let(::ReadWriteSocket)
        }
    }
}
