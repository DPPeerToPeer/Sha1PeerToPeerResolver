package org.example.sha1PeerToPeer.ui.start

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.sha1PeerToPeer.domain.models.ProgramState
import org.example.sha1PeerToPeer.ui.calculation.CalculationScreen

class StartScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<StartViewModel>()
        val state by screenModel.state.collectAsState()
        NavigateToCalculationIfProgramIsRunning(programState = state.programState)

        Button(
            onClick = {
                screenModel.onStartClick()
            },
        ) {
            Text("Start")
        }
    }
}

@Composable
private fun NavigateToCalculationIfProgramIsRunning(programState: ProgramState) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(programState) {
        when (programState) {
            ProgramState.RUNNING -> {
                navigator.push(CalculationScreen())
            }
            else -> {
            }
        }
    }
}
