package com.example.common.time

import com.lyft.kronos.Clock
import com.lyft.kronos.ClockFactory
import com.lyft.kronos.SyncListener
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val logger = KotlinLogging.logger {}

internal class TimeRepository : ITimeRepository {

    private val clock = ClockFactory.createKronosClock(
        localClock = object : Clock {
            override fun getCurrentTimeMs(): Long {
                return System.currentTimeMillis()
            }

            override fun getElapsedTimeMs(): Long {
                return System.currentTimeMillis()
            }
        },
        syncListener = object : SyncListener {
            override fun onError(host: String, throwable: Throwable) {
                logger.error {
                    "Error syncing time with $host"
                }
            }

            override fun onStartSync(host: String) {
                logger.info {
                    "Time syncing with host $host"
                }
            }

            override fun onSuccess(ticksDelta: Long, responseTimeMs: Long) {
                logger.info {
                    "Time synced in $responseTimeMs"
                }
            }
        },
        syncResponseCache = TimeSyncResponseCache(),
    )

    override fun getCurrentTime(): Long =
        clock.getCurrentTime().posixTimeMs

    override suspend fun syncTime() {
        withContext(Dispatchers.IO) {
            clock.sync()
        }
    }
}
