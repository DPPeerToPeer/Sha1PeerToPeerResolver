package com.example.socketsFacade.internal

import com.example.socketsFacade.IUdpBroadcastSocket
import com.example.socketsFacade.MessageWithIp
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

internal class UdpBroadcastSocket : IUdpBroadcastSocket {

    override fun sendBroadcastAndListenMessages(message: String): Flow<MessageWithIp> = channelFlow {
        val selectorManager = SelectorManager(dispatcher = Dispatchers.IO)
        val socket = aSocket(selectorManager).udp().bind(
            localAddress = InetSocketAddress(
                port = 1021,
                hostname = "0.0.0.0",
            ),
        )
        socket.use {
            supervisorScope {
                val address = InetSocketAddress(hostname = "255.255.255.255", port = 1021)
                val bytePacket = buildPacket { writeText(text = message) }
                socket.send(
                    Datagram(
                        packet = bytePacket,
                        address = address,
                    ),
                )
                println("Broadcast sent")
                launch(CoroutineExceptionHandler { coroutineContext, throwable -> throwable.printStackTrace() }) {
                    while (coroutineContext.isActive) {
                        println("Waiting")
                        val datagram = socket.receive()
                        println(datagram.toString())
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
