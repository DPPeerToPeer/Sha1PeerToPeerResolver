package org.example.sha1PeerToPeer.ui.start

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.sha1PeerToPeer.ui.calculation.CalculationScreen

class StartScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<StartViewModel>()
        val state by screenModel.state.collectAsState() // / by=.value
//        val state2 = screenModel.state.collectAsState().value

        val navigator = LocalNavigator.currentOrThrow

        if (state.shouldNavigateToNextScreen) {
            navigator.push(CalculationScreen(state.hashToFind))
        }

        // NavigateToCalculationIfProgramIsRunning(programState = state.programState)

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(all = 16.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Function()
                SimpleOutlinedTextFieldSample(
                    text = state.hashToFind,
                    onValueChange = {
                        screenModel.onHashChange(it)
                    },
                ) // ///w implementacji onValuechange wykorzystac viewModel
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
                Button(
                    onClick = {
//                    navigator.push(CalculationScreen(state.hashToFind))
                        screenModel.onStartClick()
                    },
                    enabled = !state.isLoading,
                ) {
                    Text("Start", modifier = Modifier.padding(all = 10.dp))
                }
            }
        }
    }
}

/*@Composable
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
}*/

@Composable
fun SimpleOutlinedTextFieldSample(
    text: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = { Text("Hash") },
    )
}

@Composable
@Preview
fun Function() {
    Text("Type hash which You are looking for and let's start!", fontSize = 30.sp, textAlign = TextAlign.Center)
}
