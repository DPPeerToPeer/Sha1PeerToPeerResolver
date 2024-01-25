package org.example.sha1PeerToPeer.ui.start

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.sha1PeerToPeer.domain.useCases.runProgram.IRunProgramUseCase

class StartViewModel(
    private val runProgramUseCase: IRunProgramUseCase,
) : StateScreenModel<StartScreenState>(
        initialState =
            StartScreenState(
                hashToFind = "",
                isLoading = false,
                shouldNavigateToNextScreen = false,
            ),
    ) {
    fun onHashChange(newHash: String) {
        mutableState.update { currentState ->
            currentState.copy(hashToFind = newHash)
        }
    }

    fun onStartClick() {
        screenModelScope.launch(Dispatchers.IO) {
            mutableState.update { currentstate ->
                currentstate.copy(isLoading = true)
            }
            runProgramUseCase.invoke(hashToFind = state.value.hashToFind)
            mutableState.update { currnetState ->
                currnetState.copy(shouldNavigateToNextScreen = true)
            }
        }
    }
}
