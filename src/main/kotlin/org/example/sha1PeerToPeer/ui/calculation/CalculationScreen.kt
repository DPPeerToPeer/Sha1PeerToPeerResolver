package org.example.sha1PeerToPeer.ui.calculation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel

class CalculationScreen(private val hashToFind: String) : Screen { // ///val bo zmienna pozostaje na pozniej

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<CalculationViewModel>()
        val state = screenModel.state.collectAsState().value

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text("Hash: $hashToFind")

            when (state) {
                is CalculationState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is CalculationState.Calculation -> {
                    LazyColumn {
                        if (state.nodes.isNotEmpty()) {
                            item {
                                Text(text = "Nodes:")
                            }
                            items(state.nodes) { node ->
                                Text(text = "${node.name} (${node.id})")
                            }
                        }
                        if (state.batches.isNotEmpty()) {
                            item {
                                Text(text = "Batches:")
                            }
                            items(state.batches.toList()) { (batch, batchState) ->
                                Text(text = "${batch.start} - ${batch.end} : $batchState")
                            }
                        }
                    }
                }
                is CalculationState.Found -> {
                    Text(text = "Result is ${state.result}")
                }
            }
        }
    }
}
