package org.example.sha1PeerToPeer.utils

import io.mockk.MockKAnnotations.init
import kotlin.test.BeforeTest

abstract class BaseTest {

    @BeforeTest
    fun setUp() = init(this)
}
