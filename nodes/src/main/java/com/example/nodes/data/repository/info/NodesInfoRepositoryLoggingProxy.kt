package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.NodeId
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

internal class NodesInfoRepositoryLoggingProxy(
    private val nodesInfoRepository: NodesInfoRepository,
) : INodesInfoRepository by nodesInfoRepository {
    override suspend fun upsertManyNodes(nodes: List<Node>) {
        logger.atDebug {
            message = "upsertManyNodes"
            payload = buildMap {
                put("nodes", nodes)
            }
        }
        nodesInfoRepository.upsertManyNodes(nodes = nodes)
    }

    override suspend fun getActiveNodes(): List<Node> =
        nodesInfoRepository.getActiveNodes().also {
            logger.atDebug {
                message = "getActiveNodes"
                payload = buildMap {
                    put("returned nodes", it)
                }
            }
        }

    override suspend fun upsertNode(node: Node) {
        logger.atDebug {
            message = "upsertNode"
            payload = buildMap {
                put("node", node)
            }
        }
        nodesInfoRepository.upsertNode(node = node)
    }

    override suspend fun removeNode(id: NodeId) {
        logger.atDebug {
            message = "removeNode"
            payload = buildMap {
                put("id", id)
            }
        }
        nodesInfoRepository.removeNode(id = id)
    }

    override suspend fun updateHealth(nodeId: NodeId, timestamp: Long) {
        logger.atDebug {
            message = "updateHealth"
            payload = buildMap {
                put("id", nodeId)
                put("timestamp", timestamp)
            }
        }
        nodesInfoRepository.updateHealth(
            nodeId = nodeId,
            timestamp = timestamp,
        )
    }
}
