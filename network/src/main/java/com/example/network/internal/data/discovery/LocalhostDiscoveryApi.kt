package com.example.network.internal.data.discovery

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.models.BroadcastMessage
import com.example.socketsFacade.IUdpBroadcastSocket
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

internal class LocalhostDiscoveryApi(
    private val udpBroadcastSocket: IUdpBroadcastSocket,
) : IDiscoveryApi {

    override fun runDiscoveryAndListenNewNodes(
        hashToFind: String,
        myId: NodeId,
        myName: String,
        myPort: Int,
    ): Flow<Node> {
        val messageToSend = BroadcastMessage(
            hashToFind = hashToFind,
            myId = myId.id,
            myName = myName,
            myPort = myPort,
        )
        return udpBroadcastSocket.sendBroadcastAndListenMessages(
            message = Json.encodeToString(
                serializer = BroadcastMessage.serializer(),
                messageToSend,
            ),
        ).mapNotNull {
            runCatching {
                Json.decodeFromString<BroadcastMessage>(it.message) to it.ip
            }.onFailure {
                coroutineContext.ensureActive()
                it.printStackTrace()
            }.getOrNull()
        }.filter { (message, _) ->
            message.hashToFind == hashToFind
        }.map { (message, ip) ->
            Node(
                id = NodeId(id = message.myId),
                ip = ip,
                port = message.myPort,
                name = message.myName,
            )
        }
    }
}
