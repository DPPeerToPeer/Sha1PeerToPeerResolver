package com.example.common.di

import com.example.common.IGetCurrentTimeUseCase
import com.example.common.IGetMyIdUseCase
import com.example.common.models.GetCurrentTimeUseCase
import com.example.common.models.GetMyIdUseCase
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val commonModule = DI.Module(name = "Common") {
    bindProvider<IGetCurrentTimeUseCase> {
        GetCurrentTimeUseCase()
    }
    bindSingleton<IGetMyIdUseCase> {
        GetMyIdUseCase()
    }
}
