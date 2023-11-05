package com.example.network

import com.example.common.models.SocketId
import kotlinx.coroutines.flow.Flow

interface IConnectionsHandler {

    fun runAndReturnLocalIpAndPort(): IpAndPort

    suspend fun sendNodeMessage(message: Pair<SocketId, NodeMessage>)

    fun listenNodesMessages(): Flow<Pair<SocketId, NodeMessage>>
}
