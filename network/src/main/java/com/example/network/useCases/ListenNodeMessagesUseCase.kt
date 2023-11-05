package com.example.network.useCases

import com.example.common.models.SocketId
import com.example.network.IConnectionsHandler
import com.example.network.NodeMessage
import kotlinx.coroutines.flow.Flow

class ListenNodeMessagesUseCase(
    private val connectionsHandler: IConnectionsHandler,
) {

    operator fun invoke(): Flow<Pair<SocketId, NodeMessage>> = connectionsHandler
        .listenNodesMessages()
}
