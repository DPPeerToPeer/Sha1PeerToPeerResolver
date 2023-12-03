package com.example.network.internal.data.nodes.singleNodeConnection

import com.example.common.models.NodeId
import com.example.network.models.NodeMessage
import com.example.socketsFacade.IReadWriteSocket
import kotlinx.coroutines.channels.Channel

internal class SingleNodeConnectionFactory : ISingleNodeConnectionFactory {

    override fun create(
        socket: IReadWriteSocket,
        messageChannel: Channel<Pair<NodeId, NodeMessage>>,
    ): ISingleNodeConnectionHandler = SingleNodeConnectionHandler(
        socket = socket,
        messageChannel = messageChannel,
    )
}
