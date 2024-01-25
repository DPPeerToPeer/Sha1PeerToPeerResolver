package com.example.socketsFacade

import kotlinx.serialization.Serializable

@Serializable
data class HttpBroadcastMessage(
    val hashToFind: String,
    val myId: String,
    val myName: String,
    val myPort: Int,
)
