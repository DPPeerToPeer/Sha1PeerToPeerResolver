package com.example.socketsFacade

import kotlinx.serialization.Serializable

@Serializable
data class HttpBroadcastResponseElement(
    val address: String,
    val hashToFind: String,
    val myId: String,
    val myName: String,
    val myPort: Int,
)
