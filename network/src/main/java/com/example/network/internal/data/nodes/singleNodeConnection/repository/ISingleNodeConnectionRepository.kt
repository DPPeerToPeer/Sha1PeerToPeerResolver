package com.example.network.internal.data.nodes.singleNodeConnection.repository

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionHandler
import com.example.socketsFacade.IReadWriteSocket

interface ISingleNodeConnectionRepository {

    suspend fun getOrCreateSingleConnectionHandlerAsClient(node: Node): ISingleNodeConnectionHandler

    suspend fun createSingleConnectionHandlerAsServer(socket: IReadWriteSocket)

    fun getIpOfSocket(nodeId: NodeId): String?
}
