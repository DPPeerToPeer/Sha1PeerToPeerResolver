package com.example.calculation.domain.useCase

import com.example.common.models.Batch
import org.junit.Test

class MakeCalculationInBatchUseCaseTest {
    @Test
    fun test1() {
        val m = MakeCalculationInBatchUseCase()
        val b = Batch("aaaa", "b5gJ")
        val hashToFind = "fbc7db55f29ca74a07cde2f642a0d356673adefd"

        System.out.println(m.invoke(b, hashToFind))
    }
}
