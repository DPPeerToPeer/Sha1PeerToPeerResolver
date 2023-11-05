package org.example.sha1PeerToPeer.connections

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import org.example.sha1PeerToPeer.domain.models.SocketId
import kotlin.coroutines.coroutineContext

class SingleNodeConnectionHandler(
    private val socket: Socket,
    val socketId: SocketId,
    private val messageChannel: Channel<Pair<SocketId, NodeMessage>>,
) {
    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel()

    suspend fun listenIncomingMessages() {
        while (coroutineContext.isActive) {
            readChannel.awaitContent()
            val incomingLine = readChannel.readUTF8Line()
            incomingLine?.let {
                val message = Json.decodeFromString<NodeMessage>(incomingLine)
                messageChannel.send(socketId to message)
            }
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
