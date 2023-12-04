package com.example.socketsFacade.internal

import com.example.socketsFacade.IReadWriteSocket
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.*

internal class ReadWriteSocket(
    private val socket: Socket,
) : IReadWriteSocket {

    private val readChannel by lazy {
        socket.openReadChannel()
    }
    private val writeChannel by lazy {
        socket.openWriteChannel()
    }
    override suspend fun readLine(): String? {
        readChannel.awaitContent()
        return readChannel.readUTF8Line()
    }

    override suspend fun write(text: String) {
        writeChannel.writeStringUtf8(s = text)
    }

    override val remoteIp: String
        get() = socket.remoteAddress.toJavaAddress().address
}
