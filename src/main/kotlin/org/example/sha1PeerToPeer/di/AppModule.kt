package org.example.sha1PeerToPeer.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.example.sha1PeerToPeer.domain.useCases.RunProgramUseCase
import org.example.sha1PeerToPeer.ui.start.StartViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val appModule = DI.Module("App") {
    bindProvider<StartViewModel> {
        StartViewModel(/*instance()*/)
    }
    bindSingleton {
        RunProgramUseCase(
            runConnectionsHandlerUseCase = instance(),
            discoveryUseCase = instance(),
            calculationRepository = instance(),
            handleIncomingNodeMessagesUseCase = instance(),
            sendHealthUseCase = instance(),
            removeNotActiveNodesUseCase = instance(),
            nodesRepository = instance(),
            nodesBroadcastRepository = instance(),
            appScope = CoroutineScope(SupervisorJob()),
        )
    }
}
