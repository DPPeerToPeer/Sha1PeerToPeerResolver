package com.example.common.time

import com.lyft.kronos.SyncResponseCache
import com.lyft.kronos.internal.Constants

internal class TimeSyncResponseCache : SyncResponseCache {

    override var currentOffset: Long = Constants.TIME_UNAVAILABLE

    override var currentTime: Long = Constants.TIME_UNAVAILABLE

    override var elapsedTime: Long = Constants.TIME_UNAVAILABLE

    override fun clear() {
        currentTime = Constants.TIME_UNAVAILABLE
        currentOffset = Constants.TIME_UNAVAILABLE
        elapsedTime = Constants.TIME_UNAVAILABLE
    }
}
