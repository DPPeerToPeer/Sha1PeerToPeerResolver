package org.example.sha1PeerToPeer.ui.start

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.sha1PeerToPeer.ui.calculation.CalculationScreen

class StartScreen : Screen {

    @Composable
    override fun Content() {
        // val screenModel = rememberScreenModel<StartViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        Button(
            onClick = {
                navigator.push(CalculationScreen())
            },
        ) {
            Text("Następny ekran")
        }
    }
}
