package com.example.calculation.domain.useCase

import com.example.calculation.IMakeCalculationInBatchUseCase
import com.example.common.models.Batch
import com.example.common.models.CalculationResult

internal class MakeCalculationInBatchUseCase : IMakeCalculationInBatchUseCase {

    override operator fun invoke(batch: Batch): CalculationResult {
        TODO()
    }
}
