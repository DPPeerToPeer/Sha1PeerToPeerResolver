package com.example.network.internal.data.nodes.singleNodeConnection

import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.messagesProxy.IMessagesProxy
import com.example.network.models.NodeMessage
import com.example.socketsFacade.IReadWriteSocket
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

private val logger = KotlinLogging.logger {}

internal class SingleNodeConnectionHandler(
    private val socket: IReadWriteSocket,
    private val messagesProxy: IMessagesProxy,
    private var nodeId: NodeId?,
) : ISingleNodeConnectionHandler {

    override suspend fun listenNodeId(): NodeId {
        logger.debug {
            "listenNodeId start"
        }
        return socket.readLine()?.let {
            NodeId(id = it)
                .also {
                    logger.atDebug {
                        message = "listenNodeId came"
                        payload = buildMap {
                            put("id", it)
                        }
                    }
                }
                .also { this.nodeId = it }
        } ?: error("listenNodeId returned null")
    }

    override suspend fun sendMyId(id: NodeId) {
        logger.atDebug {
            message = "sendMyId"
            payload = buildMap {
                put("id", id)
            }
        }
        socket.write(text = id.id).also {
            messagesProxy.onNewMessage(nodeId = nodeId!!, message = NodeMessage.InitedConnection)
        }
    }

    override suspend fun listenIncomingMessages() {
        while (coroutineContext.isActive) {
            val incomingLine = socket.readLine()
            logger.atDebug {
                message = "readLine"
                payload = buildMap {
                    put("line", incomingLine ?: "null")
                }
            }
            incomingLine?.let {
                val message = Json.decodeFromString<NodeMessage>(incomingLine)
                logger.atDebug {
                    this.message = "decoded message"
                    payload = buildMap {
                        put("message", message)
                    }
                }
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
