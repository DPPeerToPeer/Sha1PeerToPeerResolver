package org.example.sha1PeerToPeer.ui.calculation

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.calculation.ICalculationRepository
import com.example.nodes.data.repository.info.INodesInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculationViewModel(
    nodesRepository: INodesInfoRepository,
    calculationRepository: ICalculationRepository,
) : StateScreenModel<CalculationState>(
    initialState = CalculationState.Loading,
) {

    init {
        screenModelScope.launch(Dispatchers.IO) {
            nodesRepository.getActiveNodesFlow()
                .collect { nodes ->
                    mutableState.update {
                        CalculationState.Calculation(
                            nodes = nodes,
                            batches = emptyMap(),
                        )
                    }
                }
        }
    }
}
