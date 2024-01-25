package com.example.socketsFacade

interface IHttpDiscoveryClient {

    suspend fun sendMessage(message: HttpBroadcastMessage): List<HttpBroadcastResponseElement>?
}
