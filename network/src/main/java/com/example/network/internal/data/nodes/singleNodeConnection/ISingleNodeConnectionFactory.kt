package com.example.network.internal.data.nodes.singleNodeConnection

import com.example.socketsFacade.IReadWriteSocket

internal interface ISingleNodeConnectionFactory {

    fun create(
        socket: IReadWriteSocket,
    ): ISingleNodeConnectionHandler
}
