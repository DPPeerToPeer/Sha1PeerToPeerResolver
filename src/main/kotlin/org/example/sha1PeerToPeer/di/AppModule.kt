package org.example.sha1PeerToPeer.di

import org.example.sha1PeerToPeer.domain.useCases.HandleIncomingNodeMessagesUseCase
import org.example.sha1PeerToPeer.domain.useCases.ResultFoundUseCase
import org.example.sha1PeerToPeer.domain.useCases.RunRepetitiveOperationsUseCase
import org.example.sha1PeerToPeer.domain.useCases.runProgram.FakeRunProgramUseCase
import org.example.sha1PeerToPeer.domain.useCases.runProgram.IRunProgramUseCase
import org.example.sha1PeerToPeer.domain.useCases.runProgram.RunProgramUseCase
import org.example.sha1PeerToPeer.ui.calculation.CalculationViewModel
import org.example.sha1PeerToPeer.ui.start.StartViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val appModule = DI.Module("App") {
    bindProvider<StartViewModel> {
        StartViewModel(instance(tag = "Real"))
    }
    bindProvider<CalculationViewModel> {
        CalculationViewModel(
            nodesRepository = instance(),
            resultFoundUseCase = instance(),
            runProgramUseCase = instance(tag = "Real"),
        )
    }
    bindSingleton {
        ResultFoundUseCase(
            nodesBroadcastUseCase = instance(),
        )
    }
    bindSingleton<IRunProgramUseCase>(tag = "Real") {
        RunProgramUseCase(
            runConnectionsHandlerUseCase = instance(),
            appScope = instance(),
            runRepetitiveOperationsUseCase = instance(),
            syncTimeUseCase = instance(),
            calculationRepository = instance(),
        )
    }

    bindSingleton<RunRepetitiveOperationsUseCase> {
        RunRepetitiveOperationsUseCase(
            discoveryUseCase = instance(),
            calculationRepository = instance(),
            handleIncomingNodeMessagesUseCase = instance(),
            sendHealthUseCase = instance(),
            removeNotActiveNodesUseCase = instance(),
            nodesRepository = instance(),
            nodesBroadcastUseCase = instance(),
            getCurrentTimeUseCase = instance(),
            getMyIdUseCase = instance(),
            resultsFoundUseCase = instance(),
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
