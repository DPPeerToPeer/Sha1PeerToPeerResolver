package com.example.socketsFacade.internal

import com.example.socketsFacade.IReadWriteSocket
import com.example.socketsFacade.IServerSocket
import io.ktor.network.sockets.*
import io.ktor.util.network.*

internal class ServerSocketImpl(
    private val serverSocket: ServerSocket,
) : IServerSocket {

    override suspend fun accept(): IReadWriteSocket =
        serverSocket.accept().let(::ReadWriteSocket)

    override val port: Int
        get() = serverSocket.localAddress.toJavaAddress().port
}
