package com.example.calculation.domain.useCase

import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Test

internal class MakeCalculationInBatchUseCaseTest : BaseTest() {

    @MockK
    private lateinit var sha1UseCase: Sha1UseCase

    private val getAvailableCharsUseCase: GetAvailableCharsUseCase = GetAvailableCharsUseCase()

    @InjectMockKs
    private lateinit var useCase: MakeCalculationInBatchUseCase

    @Test
    fun test1() {
        val hashToFind = "hash"
        val solution = "aaba"
        val batch = Batch("aaaa", "a5gJ")

        every {
            sha1UseCase(text = any())
        } returns "other"
        every {
            sha1UseCase(text = solution)
        } returns hashToFind

        useCase.invoke(batch, hashToFind) shouldBe CalculationResult.Found(text = solution)
    }

    @Test
    fun test2() {
        val batch = Batch("aaaa", "a5gJ")
        val hashToFind = "hash"
        val solution = "a4z1"

        every {
            sha1UseCase(text = any())
        } returns "other"
        every {
            sha1UseCase(text = solution)
        } returns hashToFind

        useCase.invoke(batch, hashToFind) shouldBe CalculationResult.Found(text = solution)
    }

    @Test
    fun test3() {
        val batch = Batch("aaaa", "a5gJ")
        val hashToFind = "hash"
        val solution = "aaaaaa"

        every {
            sha1UseCase(text = any())
        } returns "other"
        every {
            sha1UseCase(text = solution)
        } returns hashToFind

        useCase.invoke(batch, hashToFind) shouldBe CalculationResult.NotFound
    }
}
