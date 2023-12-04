package com.example.calculation.di

import com.example.calculation.ICalculationRepository
import com.example.calculation.data.repository.CalculationRepository
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val calculationModule = DI.Module(name = "Calculation") {
    bindSingleton<ICalculationRepository> {
        CalculationRepository()
    }
}
