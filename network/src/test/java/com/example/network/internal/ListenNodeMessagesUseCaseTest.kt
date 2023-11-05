package com.example.network.internal

import app.cash.turbine.test
import com.example.network.internal.useCases.ListenNodeMessagesUseCase
import com.example.network.utils.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class ListenNodeMessagesUseCaseTest : BaseTest() {
    @MockK
    private lateinit var connectionsHandler: IConnectionsHandler

    @InjectMockKs
    private lateinit var listenNodeMessagesUseCase: ListenNodeMessagesUseCase

    @Test
    fun `invoke calls connectionHandler`() = runTest {
        every {
            connectionsHandler.listenNodesMessages()
        } returns flowOf()

        listenNodeMessagesUseCase.invoke().test {
            awaitComplete()
        }

        verify(exactly = 1) {
            connectionsHandler.listenNodesMessages()
        }
    }
}
