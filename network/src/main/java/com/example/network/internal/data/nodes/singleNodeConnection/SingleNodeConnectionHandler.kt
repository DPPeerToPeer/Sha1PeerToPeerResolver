package com.example.network.internal.data.nodes.singleNodeConnection

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import com.example.socketsFacade.IReadWriteSocket
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

internal class SingleNodeConnectionHandler(
    private val socket: IReadWriteSocket,
    private val messageChannel: Channel<Pair<NodeId, NodeMessage>>,
) : ISingleNodeConnectionHandler {

    private var nodeId: NodeId? = null

    override suspend fun listenNodeId(): NodeId {
        return NodeId(id = socket.readLine()!!).also { this.nodeId = it }
    }

    override suspend fun listenIncomingMessages() {
        while (coroutineContext.isActive) {
            val incomingLine = socket.readLine()
            incomingLine?.let {
                val message = Json.decodeFromString<NodeMessage>(incomingLine)
                messageChannel.send(nodeId!! to message)
            }
        }
    }

    override suspend fun writeMessage(message: NodeMessage) {
        val jsonMessage = Json.encodeToString(serializer = NodeMessage.serializer(), value = message)
        socket.write(text = jsonMessage)
    }
}
