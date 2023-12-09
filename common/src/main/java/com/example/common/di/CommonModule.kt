package com.example.common.di

import com.example.common.IGetMyIdUseCase
import com.example.common.models.GetCurrentTimeUseCase
import com.example.common.models.GetMyIdUseCase
import org.kodein.di.DI
import org.kodein.di.bindProvider

val commonModule = DI.Module(name = "Common") {
    bindProvider<GetCurrentTimeUseCase> {
        GetCurrentTimeUseCase()
    }
    bindProvider<IGetMyIdUseCase> {
        GetMyIdUseCase()
    }
}
