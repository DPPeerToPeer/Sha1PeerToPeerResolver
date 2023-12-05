package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.NodeId
import com.example.nodes.domain.models.NodeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant

internal class NodesInfoRepository : INodesInfoRepository {

    private val nodes: MutableStateFlow<Map<Node, NodeState>> = MutableStateFlow(emptyMap()) // MutableStateFlow

    override suspend fun upsertManyNodes(nodes: List<Node>) {
        for (node in nodes){
            upsertNode(node)
        }
    }

    override suspend fun getActiveNodes(): List<Node> {
        return nodes.value.keys.toList()
    }

    override suspend fun upsertNode(node: Node) {
        nodes.update {
            var map =it.toMutableMap()
            map[node] = NodeState(System.currentTimeMillis())
            map.toMap()
            }
        }


    override suspend fun removeNode(id: NodeId) {
        nodes.update {
            val mutableMap = it.toMutableMap()
            val nodeToFind = it.keys.find { it.id == id}
            mutableMap.remove(nodeToFind)
            mutableMap.toMap()
            }
        }

    override suspend fun updateHealth(nodeId: NodeId, timestamp: Long) {

        nodes.update {
            val nodesList = it.keys.toList()

            val nodeToSwap = nodesList.find { it.id == nodeId }

            if(nodeToSwap === null) return

            val mutableNodeMap = it.toMutableMap()
            mutableNodeMap[nodeToSwap] = NodeState(timestamp)

            mutableNodeMap.toMap()
        }
    }
}




