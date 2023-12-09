package com.example.common.models

import com.example.common.IGetCurrentTimeUseCase

internal class GetCurrentTimeUseCase : IGetCurrentTimeUseCase {

    override operator fun invoke(): Long = System.currentTimeMillis()
}
