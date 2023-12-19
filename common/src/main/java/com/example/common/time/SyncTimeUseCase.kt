package com.example.common.time

import com.example.common.ISyncTimeUseCase

internal class SyncTimeUseCase(
    private val timeRepository: ITimeRepository,
) : ISyncTimeUseCase {

    override suspend fun invoke() {
        timeRepository.syncTime()
    }
}
