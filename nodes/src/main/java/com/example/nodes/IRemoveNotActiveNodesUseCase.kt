package com.example.nodes

import com.example.common.models.Node

interface IRemoveNotActiveNodesUseCase {
    suspend operator fun invoke(): List<Node>
}
