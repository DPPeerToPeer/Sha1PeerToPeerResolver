package com.example.common.di

import com.example.common.models.GetCurrentTimeUseCase
import org.kodein.di.DI
import org.kodein.di.bindProvider

val commonModule = DI.Module(name = "Common") {
    bindProvider<GetCurrentTimeUseCase> {
        GetCurrentTimeUseCase()
    }
}
