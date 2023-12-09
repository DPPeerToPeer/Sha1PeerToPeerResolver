package com.example.socketsFacade

interface IServerSocket {

    suspend fun accept(): IReadWriteSocket

    val port: Int
}
