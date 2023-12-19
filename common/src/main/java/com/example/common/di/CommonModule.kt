package com.example.common.di

import com.example.common.IGetCurrentTimeUseCase
import com.example.common.IGetMyIdUseCase
import com.example.common.ISyncTimeUseCase
import com.example.common.models.GetMyIdUseCase
import com.example.common.time.GetCurrentTimeUseCase
import com.example.common.time.ITimeRepository
import com.example.common.time.SyncTimeUseCase
import com.example.common.time.TimeRepository
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val commonModule = DI.Module(name = "Common") {
    bindProvider<IGetCurrentTimeUseCase> {
        GetCurrentTimeUseCase(
            timeRepository = instance(),
        )
    }
    bindSingleton<ITimeRepository> {
        TimeRepository()
    }
    bindProvider<ISyncTimeUseCase> {
        SyncTimeUseCase(timeRepository = instance())
    }
    bindSingleton<IGetMyIdUseCase> {
        GetMyIdUseCase()
    }
}
