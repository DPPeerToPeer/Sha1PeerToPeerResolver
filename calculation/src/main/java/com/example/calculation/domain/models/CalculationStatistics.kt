package com.example.calculation.domain.models

data class CalculationStatistics(
    val availableAndInDb: Long,
    val checked: Long,
    val inProgressMine: Long,
    val inProgressOther: Long,
)
