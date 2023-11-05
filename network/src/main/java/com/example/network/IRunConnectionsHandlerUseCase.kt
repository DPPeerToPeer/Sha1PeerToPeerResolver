package com.example.network

import com.example.network.models.IpAndPort

interface IRunConnectionsHandlerUseCase {
    operator fun invoke(): IpAndPort
}
