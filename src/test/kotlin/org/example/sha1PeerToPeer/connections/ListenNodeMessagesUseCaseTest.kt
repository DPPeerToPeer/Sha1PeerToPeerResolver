package org.example.sha1PeerToPeer.connections

import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class ListenNodeMessagesUseCaseTest {
    @MockK
    private lateinit var connectionsHandler: ConnectionsHandler

    @InjectMockKs
    private lateinit var listenNodeMessagesUseCase: ListenNodeMessagesUseCase

    @BeforeTest
    fun setUp() = init(this)

    @Test
    fun `invoke calls connectionHandler`() = runBlocking {
        every {
            connectionsHandler.listenNodesMessages()
        } returns flowOf()

        listenNodeMessagesUseCase.invoke().toList()

        verify(exactly = 1) {
            connectionsHandler.listenNodesMessages()
        }
    }
}
