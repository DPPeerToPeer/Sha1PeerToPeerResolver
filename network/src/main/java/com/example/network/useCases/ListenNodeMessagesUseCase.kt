package com.example.network.useCases

import com.example.common.models.SocketId
import com.example.network.IListenNodeMessagesUseCase
import com.example.network.internal.IConnectionsHandler
import com.example.network.models.NodeMessage
import kotlinx.coroutines.flow.Flow

internal class ListenNodeMessagesUseCase(
    private val connectionsHandler: IConnectionsHandler,
) : IListenNodeMessagesUseCase {

    override operator fun invoke(): Flow<Pair<SocketId, NodeMessage>> = connectionsHandler
        .listenNodesMessages()
}
