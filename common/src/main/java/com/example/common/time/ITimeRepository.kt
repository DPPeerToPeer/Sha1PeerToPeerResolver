package com.example.common.time

interface ITimeRepository {

    fun getCurrentTime(): Long

    suspend fun syncTime()
}
