package org.example.sha1PeerToPeer.ui.calculation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class CalculationScreen : Screen {

    @Composable
    override fun Content() {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Calculation")
        }
    }
}
