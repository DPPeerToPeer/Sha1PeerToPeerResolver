package com.example.calculation.domain.useCase

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Test

internal class MakeCalculationInBatchUseCaseTest : BaseTest() {
    private val fakeSha1UseCase: FakeSha1UseCase = FakeSha1UseCase()

    private val sha1UseCase: ISha1UseCase = fakeSha1UseCase

    private val getAvailableCharsUseCase: GetAvailableCharsUseCase = GetAvailableCharsUseCase()

    @InjectMockKs
    private lateinit var useCase: MakeCalculationInBatchUseCase

    @Test
    fun test1() {
        val hashToFind = "hash"
        val solution = "aaba"
        val batch = Batch("aaaa", "a5gJ")

        fakeSha1UseCase.currentCorrect = solution

        useCase.invoke(batch, hashToFind) shouldBe CalculationResult.Found(text = solution)
    }

    @Test
    fun test2() {
        val batch = Batch("aaaa", "a5gJ")
        val hashToFind = "hash"
        val solution = "a4z1"

        fakeSha1UseCase.currentCorrect = solution

        useCase.invoke(batch, hashToFind) shouldBe CalculationResult.Found(text = solution)
    }

    @Test
    fun test3() {
        val batch = Batch("aaaa", "a5gJ")
        val hashToFind = "hash"
        val solution = "aaaaaa"

        fakeSha1UseCase.currentCorrect = solution

        useCase.invoke(batch, hashToFind) shouldBe CalculationResult.NotFound
    }
}
