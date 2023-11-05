package org.example.sha1PeerToPeer.connections

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

class NodeConnectionHandler(
    private val socket: Socket,
) {
    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel()

    fun listenIncomingMessages() = flow<NodeMessage> {
        while (coroutineContext.isActive) {
            readChannel.awaitContent()
            val incomingLine = readChannel.readUTF8Line()
        }
    }

    suspend fun writeMessage(message: NodeMessage) {
        val jsonMessage = Json.encodeToString(serializer = NodeMessage.serializer(), value = message)
        writeChannel.writeStringUtf8(jsonMessage)
    }

    fun close() {
        socket.close()
    }
}
