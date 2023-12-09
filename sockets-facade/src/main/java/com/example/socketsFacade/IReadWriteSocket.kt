package com.example.socketsFacade

interface IReadWriteSocket {
    suspend fun readLine(): String?
    suspend fun write(text: String)
}
