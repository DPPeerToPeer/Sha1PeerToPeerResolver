package com.example.calculation.domain.useCase

import io.mockk.MockKAnnotations.init
import kotlin.test.BeforeTest

abstract class BaseTest {

    @BeforeTest
    fun setUp() = init(this)
}
