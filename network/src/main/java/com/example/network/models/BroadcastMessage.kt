package com.example.network.models

import kotlinx.serialization.Serializable

@Serializable
internal data class BroadcastMessage(
    val hashToFind: String,
    val myId: String,
    val myName: String,
    val myPort: Int,
)
