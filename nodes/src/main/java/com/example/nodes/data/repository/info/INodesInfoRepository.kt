package com.example.nodes.data.repository.info

import com.example.common.models.Node
import com.example.common.models.NodeId

interface INodesInfoRepository {

    //przechowuje listę wszystkich nodów

    suspend fun upsertManyNodes(nodes: List<Node>)// jeśli jest takie same id podmienic. Jeśli nie ma takiego id - dodać.

    suspend fun getActiveNodes(): List<Node>// Zwracaca listę nodów

    suspend fun upsertNode(node: Node)// Dodaje pojedyńczy node

    suspend fun removeNode(id: NodeId)// Usuwa pojedyńczy node

    suspend fun updateHealth(
        nodeId: NodeId,
        timestamp: Long,
    ) // Dla danego node w kolekcji zamienia timestamp na podany jako argument

    suspend fun getNodeHealth(node: Node)
}
