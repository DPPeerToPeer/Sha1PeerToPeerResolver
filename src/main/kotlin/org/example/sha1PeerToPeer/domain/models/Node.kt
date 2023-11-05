package org.example.sha1PeerToPeer.domain.models

sealed interface Node {

    val socketId: SocketId

    data class UnknownNode(override val socketId: SocketId) : Node

    data class DiscoveredNode(
        override val socketId: SocketId,
        val name: String,
        val ip: String,
        val port: Int,
    ) : Node
}
