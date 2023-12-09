package com.example.nodes.data.repository.info

import com.example.common.IGetCurrentTimeUseCase
import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.nodes.domain.models.NodeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class NodesInfoRepository(
    private val getCurrentTimeUseCase: IGetCurrentTimeUseCase,
) : INodesInfoRepository {

    private val nodes: MutableStateFlow<Map<Node, NodeState>> = MutableStateFlow(emptyMap())

    override suspend fun upsertManyNodes(nodes: List<Node>) {
        for (node in nodes) {
            upsertNode(node)
        }
    }

    override suspend fun getActiveNodes(): List<Node> {
        return nodes.value.keys.toList()
    }

    override suspend fun upsertNode(node: Node) {
        nodes.update { currentNodes ->
            val currentNodeIds = currentNodes.keys.map { it.id }

            if (!currentNodeIds.contains(node.id)) {
                // Node not found, add it to the map
                currentNodes + (node to NodeState(getCurrentTimeUseCase()))
            } else {
                // Node found, update its state
                val updatedNodes = currentNodes.toMutableMap()
                val nodeToFind = currentNodes.keys.find { it.id == node.id }

                if (nodeToFind != null) {
                    updatedNodes[nodeToFind] = NodeState(getCurrentTimeUseCase())
                }

                updatedNodes.toMap()
            }
        }
    }

    override suspend fun removeNode(id: NodeId) {
        nodes.update { it ->
            val mutableMap = it.toMutableMap()
            val nodeToFind = it.keys.find { it.id == id }

            if (nodeToFind === null) it

            mutableMap.remove(nodeToFind)
            mutableMap.toMap()
        }
    }

    override suspend fun updateHealth(nodeId: NodeId, timestamp: Long) {
        nodes.update { it ->
            val nodesList = it.keys.toList()

            val nodeToSwap = nodesList.find { it.id == nodeId }

            if (nodeToSwap === null) return

            val mutableNodeMap = it.toMutableMap()
            mutableNodeMap[nodeToSwap] = NodeState(timestamp)

            mutableNodeMap.toMap()
        }
    }

    override suspend fun getNodeHealth(node: Node): NodeState {
        val nodeHealth = nodes.value[node]

        return nodeHealth!!
    }
}
