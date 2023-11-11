package org.example.sha1PeerToPeer.ui.start

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.sha1PeerToPeer.domain.models.ProgramState

class StartViewModel(
    // private val runProgramUseCase: RunProgramUseCase,
) : StateScreenModel<ScreenState>(
    initialState = ScreenState(
        programState = ProgramState.NOT_STARTED,
        hashToFind = "",
    ),
) {

    fun onStartClick() {
        screenModelScope.launch(Dispatchers.IO) {
/*            runProgramUseCase.runAndObserveProgramState(hashToFind = state.value.hashToFind)
                .collect { newProgramState ->
                    mutableState.update {
                        it.copy(programState = newProgramState)
                    }
                }*/
        }
    }
}
