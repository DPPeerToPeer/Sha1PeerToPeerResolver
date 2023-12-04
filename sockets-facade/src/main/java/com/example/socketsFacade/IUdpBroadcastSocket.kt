package com.example.socketsFacade

import kotlinx.coroutines.flow.Flow

interface IUdpBroadcastSocket {

    fun sendBroadcastAndListenMessages(message: String): Flow<MessageWithIp>
}
