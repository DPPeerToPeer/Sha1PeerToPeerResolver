package com.example.calculation.domain.useCase

import app.cash.turbine.test
import com.example.common.models.Batch
import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CreateBatchesUseCaseTest : BaseTest() {

    private val getAvailableCharsUseCase: GetAvailableCharsUseCase = GetAvailableCharsUseCase()

    private val scope = CoroutineScope(SupervisorJob())

    @InjectMockKs
    private lateinit var useCase: CreateBatchesUseCase

    @Test
    fun `invoke produces correct first 10 elements`() = runTest {
        val channel = useCase()

        channel
            .consumeAsFlow()
            .take(count = 10)
            .test {
                awaitItem() shouldBe Batch(start = "a", end = "9999")
                awaitItem() shouldBe Batch(start = "aaaaa", end = "a9999")
                awaitItem() shouldBe Batch(start = "baaaa", end = "b9999")
                awaitItem() shouldBe Batch(start = "caaaa", end = "c9999")
                awaitItem() shouldBe Batch(start = "daaaa", end = "d9999")
                awaitItem() shouldBe Batch(start = "eaaaa", end = "e9999")
                awaitItem() shouldBe Batch(start = "faaaa", end = "f9999")
                awaitItem() shouldBe Batch(start = "gaaaa", end = "g9999")
                awaitItem() shouldBe Batch(start = "haaaa", end = "h9999")
                awaitItem() shouldBe Batch(start = "iaaaa", end = "i9999")
                awaitComplete()
            }
    }
}
