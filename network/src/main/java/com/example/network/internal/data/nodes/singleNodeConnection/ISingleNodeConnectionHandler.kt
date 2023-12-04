package com.example.network.internal.data.nodes.singleNodeConnection

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage

interface ISingleNodeConnectionHandler {
    suspend fun listenNodeId(): NodeId

    suspend fun listenIncomingMessages()

    suspend fun writeMessage(message: NodeMessage)

    val socketIp: String
}
