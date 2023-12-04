package com.example.socketsFacade.internal

import com.example.socketsFacade.IUdpBroadcastSocket
import com.example.socketsFacade.MessageWithIp
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

internal class UdpBroadcastSocket : IUdpBroadcastSocket {

    override fun sendBroadcastAndListenMessages(message: String): Flow<MessageWithIp> = channelFlow {
        val selectorManager = SelectorManager(dispatcher = Dispatchers.IO)
        val socket = aSocket(selectorManager).udp().bind()
        socket.use {
            supervisorScope {
                val address = InetSocketAddress(hostname = "255.255.255.255", port = 9999)
                val bytePacket = buildPacket { writeText(text = message) }
                socket.send(
                    Datagram(
                        packet = bytePacket,
                        address = address,
                    ),
                )

                launch {
                    while (coroutineContext.isActive) {
                        val datagram = socket.receive()
                        val text = datagram.packet.readText()
                        send(
                            MessageWithIp(
                                message = text,
                                ip = datagram.address.toJavaAddress().address,
                            ),
                        )
                    }
                }
            }
        }
    }
}
