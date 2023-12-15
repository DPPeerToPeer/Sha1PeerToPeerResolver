package com.example.nodes.di

import com.example.nodes.INodesBroadcastUseCase
import com.example.nodes.IRemoveNotActiveNodesUseCase
import com.example.nodes.ISendHealthUseCase
import com.example.nodes.data.repository.info.INodesInfoRepository
import com.example.nodes.data.repository.info.NodesInfoRepository
import com.example.nodes.domain.useCase.NodesBroadcastUseCase
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
    bindProvider<ISendHealthUseCase> {
        SendHealthUseCase(
            nodesBroadcastUseCase = instance(),
            getCurrentTimeUseCase = instance(),
        )
    }
    bindProvider<IRemoveNotActiveNodesUseCase> {
        RemoveNotActiveNodesUseCase(
            nodesInfoRepository = instance(),
            getCurrentTimeUseCase = instance(),
        )
    }
    bindProvider<INodesBroadcastUseCase> {
        NodesBroadcastUseCase(
            sendNodeMessageUseCase = instance(),
            nodesInfoRepository = instance(),
        )
    }
}
