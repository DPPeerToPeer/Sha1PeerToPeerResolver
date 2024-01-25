package com.example.network.internal.data.discovery

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.socketsFacade.HttpBroadcastMessage
import com.example.socketsFacade.HttpBroadcastResponseElement
import com.example.socketsFacade.IHttpDiscoveryClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class OverNetworkDiscoveryApi(
    private val httpDiscoveryClient: IHttpDiscoveryClient,
) : IDiscoveryApi {

    override fun runDiscoveryAndListenNewNodes(
        hashToFind: String,
        myId: NodeId,
        myName: String,
        myPort: Int,
    ): Flow<Node> {
        return flow {
            val messageToSend = HttpBroadcastMessage(
                hashToFind = hashToFind,
                myId = myId.id,
                myName = myName,
                myPort = myPort,
            )

            val result = httpDiscoveryClient.sendMessage(
                message = messageToSend,
            )

            result?.toDomain()?.forEach {
                emit(it)
            }
        }
    }

    private fun List<HttpBroadcastResponseElement>.toDomain() = map {
        Node(
            id = NodeId(id = it.myId),
            ip = it.address,
            port = it.myPort,
            name = it.myName,
        )
    }
}
