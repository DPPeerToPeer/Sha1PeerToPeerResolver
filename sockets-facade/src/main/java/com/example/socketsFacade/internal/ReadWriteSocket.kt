package com.example.socketsFacade.internal

import com.example.socketsFacade.IReadWriteSocket
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class ReadWriteSocket(
    private val socket: Socket,
) : IReadWriteSocket {

    private val mutex by lazy {
        Mutex()
    }

    private val readChannel: ByteReadChannel by lazy {
        socket.openReadChannel()
    }
    private val writeChannel: ByteWriteChannel by lazy {
        socket.openWriteChannel()
    }
    override suspend fun readLine(): String? {
        return readChannel.readUTF8Line()
    }

    override suspend fun write(text: String) {
        mutex.withLock {
            writeChannel.writeStringUtf8(s = text + "\n")
            writeChannel.flush()
        }
    }

    override val remoteIp: String
        get() = socket.remoteAddress.toJavaAddress().address
}
