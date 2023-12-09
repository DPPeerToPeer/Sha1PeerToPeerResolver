package com.example.socketsFacade.internal

import com.example.socketsFacade.IUdpBroadcastSocket
import com.example.socketsFacade.MessageWithIp
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

private val logger = KotlinLogging.logger {}

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
                logger.debug {
                    "Broadcast sent"
                }
                launch(
                    CoroutineExceptionHandler { coroutineContext, throwable ->
                        logger.warn(throwable = throwable) {
                            "Error in Receiving message coroutine"
                        }
                    },
                ) {
                    while (coroutineContext.isActive) {
                        logger.debug {
                            "Waiting for message"
                        }
                        val datagram = socket.receive()
                        val text = datagram.packet.readText()
                        val ip = datagram.address.toJavaAddress().address

                        logger.atDebug {
                            this.message = "UDP packet received"
                            payload = buildMap {
                                put("message", text)
                                put("ip", ip)
                            }
                        }
                        send(
                            MessageWithIp(
                                message = text,
                                ip = ip,
                            ),
                        )
                    }
                }
            }
        }
    }
}
