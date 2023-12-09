package com.example.network.internal.data.nodes.singleNodeConnection.factory

import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionHandler
import com.example.socketsFacade.IReadWriteSocket

internal interface ISingleNodeConnectionFactory {

    fun create(
        socket: IReadWriteSocket,
    ): ISingleNodeConnectionHandler
}
