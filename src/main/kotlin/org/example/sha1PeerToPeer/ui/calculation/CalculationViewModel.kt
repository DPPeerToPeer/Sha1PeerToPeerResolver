package org.example.sha1PeerToPeer.ui.calculation

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.calculation.ICalculationRepository
import com.example.nodes.data.repository.info.INodesInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
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
            combine(
                nodesRepository.getActiveNodesFlow(),
                calculationRepository.batches,
            ) { nodes, batches ->
                mutableState.update {
                    CalculationState.Calculation(
                        nodes = nodes,
                        batches = batches,
                    )
                }
            }.collect {}
        }
    }
}
