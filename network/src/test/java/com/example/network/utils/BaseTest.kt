package com.example.network.utils

import io.mockk.MockKAnnotations.init
import kotlin.test.BeforeTest

abstract class BaseTest {

    @BeforeTest
    fun setUp() = init(this)
}
