package com.example.network.internal.data.nodes

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.messagesProxy.IMessagesProxy
import com.example.network.internal.data.nodes.myPort.IMyPortRepository
import com.example.network.internal.data.nodes.singleNodeConnection.repository.ISingleNodeConnectionRepository
import com.example.network.models.NodeMessage
import com.example.network.models.Port
import com.example.socketsFacade.IReadWriteSocket
import com.example.socketsFacade.IServerSocketFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

private val logger = KotlinLogging.logger {}

internal class ConnectionsHandler(
    private val scope: CoroutineScope,
    private val serverSocketFactory: IServerSocketFactory,
    private val messagesProxy: IMessagesProxy,
    private val singleNodeConnectionRepository: ISingleNodeConnectionRepository,
    private val myPortRepository: IMyPortRepository,
) : IConnectionsHandler {

    override fun runAndReturnPort(): Port {
        val serverSocket = serverSocketFactory.create()

        scope.launch {
            supervisorScope {
                while (coroutineContext.isActive) {
                    val socket: IReadWriteSocket = serverSocket.accept()
                    logger.atDebug {
                        message = "New socket accepted"
                        payload = buildMap {
                            put("ip", socket.remoteIp)
                        }
                    }
                    launch {
                        singleNodeConnectionRepository.createSingleConnectionHandlerAsServer(socket = socket)
                    }
                }
            }
        }
        return Port(port = serverSocket.port)
            .also { myPortRepository.setMyPort(port = it) }
            .also {
                logger.debug {
                    "Server started with port $it"
                }
            }
    }

    override fun getIpOfSocket(nodeId: NodeId): String? =
        singleNodeConnectionRepository.getIpOfSocket(nodeId = nodeId)

    override suspend fun sendNodeMessage(node: Node, message: NodeMessage) {
        singleNodeConnectionRepository.getOrCreateSingleConnectionHandlerAsClient(node = node)
            .writeMessage(message = message)
    }

    override fun listenNodesMessages(): Flow<Pair<NodeId, NodeMessage>> = messagesProxy
        .listenMessages()
        .onEach {
            logger.atDebug {
                message = "New message listened"
                payload = buildMap {
                    put("nodeId", it.first)
                    put("message", it.second)
                }
            }
        }
}
