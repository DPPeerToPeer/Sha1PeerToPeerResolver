package com.example.calculation.data.repository.db

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.Database
import com.example.calculation.BatchDb
import com.example.calculation.domain.models.BatchState
import com.example.calculation.domain.useCase.CreateBatchesUseCase
import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import com.example.common.models.NodeId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class CalculationDao(
    createBatchesUseCase: CreateBatchesUseCase,
) : ICalculationDao {
    private val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:test.db")

    private val batchesChannel by lazy {
        createBatchesUseCase()
    }

    init {
        // Database.Schema.create(driver)
    }
    private val db = Database(
        driver = driver,
        BatchDbAdapter = BatchDb.Adapter(
            stateAdapter = EnumColumnAdapter(),
        ),
    )

    override suspend fun getBatchState(batch: Batch): BatchState = withContext(Dispatchers.IO) {
        db.batchDbQueries.selectByStartEnd(start = batch.start, end = batch.end)
            .executeAsOneOrNull()
            ?.toDomainBatchState() ?: BatchState.Available
    }

    override suspend fun getAvailableBatchAndMarkMine(timestamp: Long): Batch? = withContext(Dispatchers.IO) {
        val batchFromDb = db.batchDbQueries.transactionWithResult {
            db.batchDbQueries.selectAvailable().executeAsOneOrNull()?.also { availableBatch ->
                db.batchDbQueries.changeStateOfBatch(
                    state = BatchStateDB.MINE,
                    otherNodeId = null,
                    timestamp = timestamp,
                    batchId = availableBatch.id,
                    found_word = null,
                )
            }?.toDomain()
        }

        batchFromDb ?: getNextBatchIfNotInDb()
    }

    private suspend fun getNextBatchIfNotInDb(): Batch? {
        val nextBatch = batchesChannel.receive()
        val resultOrNull = db.batchDbQueries.selectByStartEnd(
            start = nextBatch.start,
            end = nextBatch.end,
        ).executeAsOneOrNull()

        return if (resultOrNull != null) {
            getNextBatchIfNotInDb()
        } else {
            nextBatch
        }
    }

    override suspend fun markBatchInProgressIfWasFirst(batch: Batch, nodeId: NodeId, timestamp: Long) = withContext(Dispatchers.IO) {
        db.batchDbQueries.transaction {
            db.batchDbQueries.selectByStartEnd(start = batch.start, end = batch.end).executeAsOneOrNull()?.let { current ->
                when (val currentState = current.toDomainBatchState()) {
                    is BatchState.Checked -> {}
                    is BatchState.InProgressOtherNode -> {
                        if (currentState.startTimestamp < timestamp) {
                            db.batchDbQueries.changeStateOfBatchByStartEnd(
                                start = batch.start,
                                end = batch.end,
                                state = BatchStateDB.TAKEN_OTHER_NODE,
                                timestamp = timestamp,
                                otherNodeId = nodeId.id,
                                found_word = null,
                            )
                        }
                    }
                    is BatchState.InProgressMine -> {
                        if (currentState.startTimestamp < timestamp) {
                            db.batchDbQueries.changeStateOfBatchByStartEnd(
                                start = batch.start,
                                end = batch.end,
                                state = BatchStateDB.TAKEN_OTHER_NODE,
                                timestamp = timestamp,
                                otherNodeId = nodeId.id,
                                found_word = null,
                            )
                        }
                    }
                    is BatchState.Available -> {
                        db.batchDbQueries.changeStateOfBatchByStartEnd(
                            start = batch.start,
                            end = batch.end,
                            state = BatchStateDB.TAKEN_OTHER_NODE,
                            timestamp = timestamp,
                            otherNodeId = nodeId.id,
                            found_word = null,
                        )
                    }
                }
            } ?: run {
                db.batchDbQueries.insert(
                    start = batch.start,
                    end = batch.end,
                    state = BatchStateDB.TAKEN_OTHER_NODE,
                    timestampTaken = timestamp,
                    otherNodeId = nodeId.id,
                    found_word = null,
                )
            }
        }
    }

    override suspend fun markBatchChecked(batch: Batch) {
        db.batchDbQueries.changeStateOfBatchByStartEnd(
            state = BatchStateDB.CHECKED,
            timestamp = null,
            otherNodeId = null,
            found_word = null,
            start = batch.start,
            end = batch.end,
        )
    }

    override suspend fun markBatchesOfThisNodeAvailable(nodeId: NodeId) {
        db.batchDbQueries.transaction {
            val batchesToFree = db.batchDbQueries.selectBatchesByOtherNodeId(otherNodeId = nodeId.id)
                .executeAsList()
                .map { it.id }

            db.batchDbQueries.markBatchesAvailable(batchIds = batchesToFree)
        }
    }

    override suspend fun insertBatch(batch: Batch) {
        withContext(Dispatchers.IO) {
            db.batchDbQueries.insert(
                start = batch.start,
                end = batch.end,
                state = BatchStateDB.AVAILABLE,
                timestampTaken = null,
                otherNodeId = null,
                found_word = null,
            )
        }
    }

    override suspend fun clearDb() {
        withContext(Dispatchers.IO) {
            db.batchDbQueries.removeAll()
        }
    }

    private fun BatchDb.toDomain(): Batch = Batch(
        start = start,
        end = end,
    )

    private fun BatchDb.toDomainBatchState(): BatchState = when (state) {
        BatchStateDB.AVAILABLE -> BatchState.Available
        BatchStateDB.MINE -> {
            checkNotNull(timestampTaken)
            BatchState.InProgressMine(startTimestamp = timestampTaken)
        }
        BatchStateDB.TAKEN_OTHER_NODE -> {
            checkNotNull(timestampTaken)
            checkNotNull(otherNodeId)

            BatchState.InProgressOtherNode(
                startTimestamp = timestampTaken,
                nodeId = NodeId(id = otherNodeId),
            )
        }

        BatchStateDB.CHECKED -> BatchState.Checked(
            result = if (found_word != null) {
                CalculationResult.Found(text = found_word)
            } else {
                CalculationResult.NotFound
            },
        )
    }
}
