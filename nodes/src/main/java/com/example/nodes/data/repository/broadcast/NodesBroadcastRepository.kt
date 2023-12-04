package com.example.nodes.data.repository.broadcast

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.network.ISendNodeMessageUseCase

class NodesBroadcastRepository(
    private val sendNodeMessageUseCase: ISendNodeMessageUseCase
): INodesBroadcastRepository {

    override suspend fun sendMyInfo(port: Int) {
        TODO("Not yet implemented") // Tego jeszcze nie robic
    }

    override suspend fun sendStart() {
        TODO("Not yet implemented")
    }

    override suspend fun sendStartedCalculation(batch: Batch, timestamp: Long) { // dla każdej metody przeiterować po liśćie aktywnych node
        // wywołać invoke dla każdego node z odpowiednią instancją message
        TODO("Not yet implemented")
//        sendNodeMessageUseCase.invoke()
    }

    override suspend fun sendEndedCalculation(batch: Batch, calculationResult: CalculationResult) {
        TODO("Not yet implemented")
    }

    override suspend fun sendHealth(timestamp: Long) {
        TODO("Not yet implemented")
    }
}