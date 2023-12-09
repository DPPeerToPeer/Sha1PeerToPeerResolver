package com.example.network.internal.useCase

import com.example.common.models.Node
import com.example.network.ISendNodeMessageUseCase
import com.example.network.internal.data.nodes.IConnectionsHandler
import com.example.network.models.NodeMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

private val logger = KotlinLogging.logger {}

internal class SendNodeMessageUseCase(
    private val connectionsHandler: IConnectionsHandler,
) : ISendNodeMessageUseCase {

    override suspend operator fun invoke(node: Node, message: NodeMessage) {
        runCatching {
            connectionsHandler.sendNodeMessage(
                node = node,
                message = message,
            )
        }.onSuccess {
            logger.atDebug {
                this.message = "message sending success"
                payload = buildMap {
                    put("message sent", message)
                    put("node", node)
                }
            }
        }.onFailure {
            coroutineContext.ensureActive()
            logger.atError {
                this.message = "error sending message"
                cause = it
                payload = buildMap {
                    put("message to send", message)
                    put("node", node)
                }
            }
        }
    }
}
