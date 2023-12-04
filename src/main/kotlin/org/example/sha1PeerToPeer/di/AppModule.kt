package org.example.sha1PeerToPeer.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.example.sha1PeerToPeer.domain.useCases.HandleIncomingNodeMessagesUseCase
import org.example.sha1PeerToPeer.domain.useCases.runProgram.FakeRunProgramUseCase
import org.example.sha1PeerToPeer.domain.useCases.runProgram.IRunProgramUseCase
import org.example.sha1PeerToPeer.domain.useCases.runProgram.RunProgramUseCase
import org.example.sha1PeerToPeer.ui.start.StartViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val appModule = DI.Module("App") {
    bindProvider<StartViewModel> {
        StartViewModel(instance(tag = "Fake"))
    }
    bindSingleton<IRunProgramUseCase>(tag = "Real") {
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

    bindSingleton<IRunProgramUseCase>(tag = "Fake") {
        FakeRunProgramUseCase()
    }

    bindProvider<HandleIncomingNodeMessagesUseCase> {
        HandleIncomingNodeMessagesUseCase(
            listenNodeMessagesUseCase = instance(),
            calculationRepository = instance(),
            nodesRepository = instance(),
            getIpOfNodeUseCase = instance(),
        )
    }
}
