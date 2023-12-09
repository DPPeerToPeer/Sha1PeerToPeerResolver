package com.example.network.internal.data.nodes.messagesProxy

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal class MessagesProxy : IMessagesProxy {

    private val messageChannel: Channel<Pair<NodeId, NodeMessage>> = Channel(Channel.BUFFERED)

    override suspend fun onNewMessage(nodeId: NodeId, message: NodeMessage) {
        messageChannel.send(nodeId to message)
    }

    override fun listenMessages(): Flow<Pair<NodeId, NodeMessage>> = messageChannel
        .receiveAsFlow()
}
