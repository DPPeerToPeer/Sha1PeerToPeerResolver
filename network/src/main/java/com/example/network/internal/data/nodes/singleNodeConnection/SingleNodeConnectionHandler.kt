package com.example.network.internal.data.nodes.singleNodeConnection

import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.messagesProxy.IMessagesProxy
import com.example.network.models.NodeMessage
import com.example.socketsFacade.IReadWriteSocket
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

internal class SingleNodeConnectionHandler(
    private val socket: IReadWriteSocket,
    private val messagesProxy: IMessagesProxy,
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
                messagesProxy.onNewMessage(
                    nodeId = nodeId!!,
                    message = message,
                )
            }
        }
    }

    override suspend fun writeMessage(message: NodeMessage) {
        val jsonMessage = Json.encodeToString(serializer = NodeMessage.serializer(), value = message)
        socket.write(text = jsonMessage)
    }

    override val socketIp: String
        get() = socket.remoteIp
}
