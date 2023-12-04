package com.example.nodes.data.repository.broadcast

import com.example.common.models.Batch
import com.example.common.models.CalculationResult

internal class NodesBroadcastRepository : INodesBroadcastRepository {
    override suspend fun sendMyInfo(port: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun sendStart() {
        TODO("Not yet implemented")
    }

    override suspend fun sendStartedCalculation(batch: Batch, timestamp: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun sendEndedCalculation(batch: Batch, calculationResult: CalculationResult) {
        TODO("Not yet implemented")
    }

    override suspend fun sendHealth(timestamp: Long) {
        TODO("Not yet implemented")
    }
}
