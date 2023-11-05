package com.example.network.useCases

import com.example.network.IConnectionsHandler
import com.example.network.IpAndPort

class RunConnectionsHandlerUseCase(
    private val connectionsHandler: IConnectionsHandler,
) {

    operator fun invoke(): IpAndPort = connectionsHandler
        .runAndReturnLocalIpAndPort()
}
