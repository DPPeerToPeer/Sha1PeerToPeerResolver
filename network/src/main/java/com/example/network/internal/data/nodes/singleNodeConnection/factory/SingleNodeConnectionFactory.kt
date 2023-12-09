package com.example.network.internal.data.nodes.singleNodeConnection.factory

import com.example.network.internal.data.nodes.messagesProxy.IMessagesProxy
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionHandler
import com.example.network.internal.data.nodes.singleNodeConnection.SingleNodeConnectionHandler
import com.example.socketsFacade.IReadWriteSocket

internal class SingleNodeConnectionFactory(
    private val messagesProxy: IMessagesProxy,
) : ISingleNodeConnectionFactory {

    override fun create(
        socket: IReadWriteSocket,
    ): ISingleNodeConnectionHandler = SingleNodeConnectionHandler(
        socket = socket,
        messagesProxy = messagesProxy,
    )
}
