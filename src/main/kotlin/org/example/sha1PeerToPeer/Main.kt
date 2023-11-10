package org.example.sha1PeerToPeer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
@Preview
fun FirstPage() {
    val viewModel = remember {
        AppViewModel()
    }
    var text by remember { mutableStateOf("start!") }

    MaterialTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(all = 16.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Function()
                SimpleOutlinedTextFieldSample(onValueChange = {
                    viewModel.onHashChange(it)
                }) // ///w implementacji onValuechange wykorzystac viewModel
                Button(onClick = {
                    text = "Hello, Desktop!"
                }) {
                    Text(text, modifier = Modifier.padding(all = 10.dp))
                }
            }
        }
    }
}

@Composable
@Preview
fun Function() {
    Text("Type hash which You are looking for and let's start!", fontSize = 30.sp, textAlign = TextAlign.Center)
}

@Composable
fun SimpleOutlinedTextFieldSample(onValueChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },

        label = { Text("Hash") },
    )
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Starting Page",
        state = WindowState(width = 400.dp, height = 400.dp),
    ) {
        FirstPage()
    }
}
