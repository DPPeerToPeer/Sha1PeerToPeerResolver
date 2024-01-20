package org.example.sha1PeerToPeer.ui.calculation

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.nodes.data.repository.info.INodesInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.sha1PeerToPeer.domain.useCases.ResultFoundUseCase
import org.example.sha1PeerToPeer.domain.useCases.runProgram.IRunProgramUseCase

internal class CalculationViewModel(
    nodesRepository: INodesInfoRepository,
    private val resultFoundUseCase: ResultFoundUseCase,
    private val runProgramUseCase: IRunProgramUseCase,
) : StateScreenModel<CalculationState>(
    initialState = CalculationState.Loading,
) {

    init {
        screenModelScope.launch(Dispatchers.IO) {
            nodesRepository.getActiveNodesFlow()
                .collect { nodes ->
                    mutableState.update {
                        if (it !is CalculationState.Found) {
                            CalculationState.Calculation(
                                nodes = nodes,
                                batches = emptyMap(),
                            )
                        } else {
                            it
                        }
                    }
                }
        }

        screenModelScope.launch(Dispatchers.IO) {
            val solution = resultFoundUseCase.solutionFlow.filterNotNull()
                .first()

            mutableState.update {
                CalculationState.Found(result = solution)
            }

            runProgramUseCase.cancelOperations()
        }
    }
}
