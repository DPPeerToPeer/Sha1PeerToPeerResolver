package org.example.sha1PeerToPeer.domain.useCases

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.nodes.INodesBroadcastUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ResultFoundUseCase(
    private val nodesBroadcastUseCase: INodesBroadcastUseCase,
) {

    private val _solutionFlow = MutableStateFlow<String?>(null)

    val solutionFlow: StateFlow<String?> = _solutionFlow.asStateFlow()

    suspend operator fun invoke(batch: Batch, solution: String) = coroutineScope {
        _solutionFlow.value = solution
        nodesBroadcastUseCase.sendEndedCalculation(
            batch = batch,
            calculationResult = CalculationResult.Found(text = solution),
        )
    }
}
