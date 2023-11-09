package com.example.network

import com.example.network.models.Port

interface IRunConnectionsHandlerUseCase {
    operator fun invoke(): Port
}
