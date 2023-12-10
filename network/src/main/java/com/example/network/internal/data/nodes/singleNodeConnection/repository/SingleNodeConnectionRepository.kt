package com.example.network.internal.data.nodes.singleNodeConnection.repository

import com.example.common.IGetMyIdUseCase
import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.network.internal.data.nodes.myPort.IMyPortRepository
import com.example.network.internal.data.nodes.singleNodeConnection.ISingleNodeConnectionHandler
import com.example.network.internal.data.nodes.singleNodeConnection.factory.ISingleNodeConnectionFactory
import com.example.network.models.NodeMessage
import com.example.socketsFacade.IClientSocketFactory
import com.example.socketsFacade.IReadWriteSocket
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.coroutineContext

private val logger = KotlinLogging.logger {}

internal class SingleNodeConnectionRepository(
    private val scope: CoroutineScope,
    private val singleNodeConnectionFactory: ISingleNodeConnectionFactory,
    private val clientSocketFactory: IClientSocketFactory,
    private val myPortRepository: IMyPortRepository,
    private val getMyIdUseCase: IGetMyIdUseCase,
) : ISingleNodeConnectionRepository {

    private val sockets: ConcurrentHashMap<NodeId, ISingleNodeConnectionHandler> = ConcurrentHashMap()

    private val nodesSynchronization = ConcurrentHashMap<NodeId, Mutex>()

    override suspend fun getOrCreateSingleConnectionHandlerAsClient(node: Node): ISingleNodeConnectionHandler {
        val nodeMutex = nodesSynchronization.computeIfAbsent(node.id) {
            Mutex()
        }
        return nodeMutex.withLock {
            sockets[node.id] ?: createClientSocketAndRunHandlingMessages(node)
        }
    }

    override suspend fun createSingleConnectionHandlerAsServer(socket: IReadWriteSocket) {
        val singleConnectionsHandler = singleNodeConnectionFactory.create(
            socket = socket,
        )
        logger.debug {
            "createSingleConnectionHandlerAsServer listenNodeId"
        }
        val nodeId = singleConnectionsHandler.listenNodeId()
        logger.atDebug {
            message = "listenedId"
            payload = buildMap {
                put("nodeId", nodeId)
            }
        }
        addConnectionHandler(
            nodeId = nodeId,
            singleNodeConnectionHandler = singleConnectionsHandler,
        )

        runListeningMessages(
            nodeId = nodeId,
            singleNodeConnectionHandler = singleConnectionsHandler,
        )
    }

    override fun getIpOfSocket(nodeId: NodeId): String? =
        sockets[nodeId]?.socketIp

    private suspend fun createClientSocketAndRunHandlingMessages(
        node: Node,
    ): ISingleNodeConnectionHandler {
        logger.debug {
            "createClientSocketAndRunHandlingMessages"
        }
        val socket = clientSocketFactory.create(
            ip = node.ip,
            port = node.port,
        )
        val singleConnectionsHandler = singleNodeConnectionFactory.create(
            socket = socket,
            nodeId = node.id,
        )

        val myId = getMyIdUseCase()
        singleConnectionsHandler.sendMyId(myId)

        logger.atDebug {
            message = "sendMyId"
            payload = buildMap {
                put("id", myId)
            }
        }

        val discoveryMessage = NodeMessage.Discovery(
            port = myPortRepository.getMyPortOrThrow().port,
            id = myId.id,
            name = "myName",
        )

        singleConnectionsHandler.writeMessage(
            message = discoveryMessage,
        )

        logger.atDebug {
            message = "send discovery"
            payload = buildMap {
                put("message", discoveryMessage)
            }
        }

        addConnectionHandler(
            nodeId = node.id,
            singleNodeConnectionHandler = singleConnectionsHandler,
        )

        scope.launch {
            runListeningMessages(
                nodeId = node.id,
                singleNodeConnectionHandler = singleConnectionsHandler,
            )
        }
        return singleConnectionsHandler
    }

    private suspend fun runListeningMessages(
        nodeId: NodeId,
        singleNodeConnectionHandler: ISingleNodeConnectionHandler,
    ) {
        runCatching {
            singleNodeConnectionHandler.listenIncomingMessages()
        }.onFailure {
            coroutineContext.ensureActive()
            removeConnectionHandler(nodeId = nodeId)
            logger.error(throwable = it) {
                "Listening messages error for $nodeId"
            }
        }
    }

    private fun addConnectionHandler(nodeId: NodeId, singleNodeConnectionHandler: ISingleNodeConnectionHandler) {
        sockets[nodeId] = singleNodeConnectionHandler
    }

    private fun removeConnectionHandler(nodeId: NodeId) {
        sockets.remove(nodeId)
    }
}
