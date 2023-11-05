package com.example.common.models

class GetCurrentTimeUseCase {

    operator fun invoke(): Long = System.currentTimeMillis()
}
