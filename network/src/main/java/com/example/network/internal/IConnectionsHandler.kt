package com.example.network.internal

import com.example.common.models.SocketId
import com.example.network.models.IpAndPort
import com.example.network.models.NodeMessage
import kotlinx.coroutines.flow.Flow

internal interface IConnectionsHandler {

    fun runAndReturnLocalIpAndPort(): IpAndPort

    suspend fun sendNodeMessage(message: Pair<SocketId, NodeMessage>)

    fun listenNodesMessages(): Flow<Pair<SocketId, NodeMessage>>
}
