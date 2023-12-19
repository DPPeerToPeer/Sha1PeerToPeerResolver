package com.example.common.time

import com.example.common.IGetCurrentTimeUseCase

internal class GetCurrentTimeUseCase(
    private val timeRepository: ITimeRepository,
) : IGetCurrentTimeUseCase {

    override operator fun invoke(): Long = timeRepository.getCurrentTime()
}
