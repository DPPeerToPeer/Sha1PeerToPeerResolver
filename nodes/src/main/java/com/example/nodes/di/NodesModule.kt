package com.example.nodes.di

import com.example.nodes.data.repository.broadcast.INodesBroadcastRepository
import com.example.nodes.data.repository.broadcast.NodesBroadcastRepository
import com.example.nodes.data.repository.info.INodesInfoRepository
import com.example.nodes.data.repository.info.NodesInfoRepository
import com.example.nodes.domain.useCase.RemoveNotActiveNodesUseCase
import com.example.nodes.domain.useCase.SendHealthUseCase
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val nodesModule = DI.Module(name = "Nodes") {
    bindSingleton<INodesInfoRepository> {
        NodesInfoRepository(getCurrentTimeUseCase = instance())
    }
    bindProvider<SendHealthUseCase> {
        SendHealthUseCase(
            nodesApi = instance(),
            getCurrentTimeUseCase = instance(),
        )
    }
    bindProvider<RemoveNotActiveNodesUseCase> {
        RemoveNotActiveNodesUseCase(
            nodesInfoRepository = instance(),
            getCurrentTimeUseCase = instance(),
        )
    }
    bindProvider<INodesBroadcastRepository> {
        NodesBroadcastRepository(
            sendNodeMessageUseCase = instance(),
            nodesInfoRepository = instance(),
        )
    }
}
