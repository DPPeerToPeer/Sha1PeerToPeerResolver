package com.example.calculation

import com.example.common.models.Batch
import com.example.common.models.CalculationResult

interface IMakeCalculationInBatchUseCase {
    suspend operator fun invoke(batch: Batch, hashToFind: String): CalculationResult
}
