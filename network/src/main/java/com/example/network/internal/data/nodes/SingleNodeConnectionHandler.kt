package com.example.network.internal.data.nodes

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

internal class SingleNodeConnectionHandler(
    private val socket: Socket,
    val nodeId: NodeId,
    private val messageChannel: Channel<Pair<NodeId, NodeMessage>>,
) {
    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel()

    suspend fun listenIncomingMessages() {
        while (coroutineContext.isActive) {
            readChannel.awaitContent()
            val incomingLine = readChannel.readUTF8Line()
            incomingLine?.let {
                val message = Json.decodeFromString<NodeMessage>(incomingLine)
                messageChannel.send(nodeId to message)
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
