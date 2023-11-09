package com.example.network.internal.useCase

import com.example.network.IRunConnectionsHandlerUseCase
import com.example.network.internal.data.nodes.IConnectionsHandler
import com.example.network.models.Port

internal class RunConnectionsHandlerUseCase(
    private val connectionsHandler: IConnectionsHandler,
) : IRunConnectionsHandlerUseCase {

    override operator fun invoke(): Port = connectionsHandler
        .runAndReturnPort()
}
