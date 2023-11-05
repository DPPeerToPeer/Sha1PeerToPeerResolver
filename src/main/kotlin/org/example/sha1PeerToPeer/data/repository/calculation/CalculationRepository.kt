package org.example.sha1PeerToPeer.data.repository.calculation

import com.example.common.models.Batch
import com.example.common.models.SocketId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import org.example.sha1PeerToPeer.data.api.nodes.INodesApi
import org.example.sha1PeerToPeer.domain.models.BatchState

class CalculationRepository(
    private val nodesApi: INodesApi,
) : ICalculationRepository {

    override val batches: StateFlow<Map<Batch, BatchState>>
        get() = _batches.asStateFlow()

    private val _batches = MutableStateFlow<Map<Batch, BatchState>>(emptyMap())

    private val mutex = Mutex()

    override suspend fun startCalculation(batch: Batch) {
        TODO("Not yet implemented")
    }

    override suspend fun getAvailableBatchAndMarkMine(): Batch? {
        TODO("Not yet implemented")
    }

    override suspend fun isBatchTakenByOtherNode(batch: Batch): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun markBatchInProgressIfWasFirst(batch: Batch, nodeId: SocketId, timestamp: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun markBatchChecked(batch: Batch) {
        TODO("Not yet implemented")
    }
}
