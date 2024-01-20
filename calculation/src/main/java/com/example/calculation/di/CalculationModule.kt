package com.example.calculation.di

import com.example.calculation.ICalculationRepository
import com.example.calculation.data.repository.CalculationRepository
import com.example.calculation.data.repository.db.CalculationDao
import com.example.calculation.data.repository.db.ICalculationDao
import com.example.calculation.domain.useCase.MakeCalculationInBatchUseCase
import com.example.calculation.domain.useCase.Sha1UseCase
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val calculationModule = DI.Module(name = "Calculation") {
    bindSingleton<ICalculationRepository> {
        CalculationRepository(
            dao = instance(),
            getCurrentTimeUseCase = instance(),
            makeCalculationInBatchUseCase = MakeCalculationInBatchUseCase(
                sha1UseCase = Sha1UseCase(),
            ),
        )
    }

    bindSingleton<ICalculationDao> {
        CalculationDao()
    }
}
