package com.example.socketsFacade

interface IClientSocketFactory {
    suspend fun create(ip: String, port: Int): IReadWriteSocket
}
