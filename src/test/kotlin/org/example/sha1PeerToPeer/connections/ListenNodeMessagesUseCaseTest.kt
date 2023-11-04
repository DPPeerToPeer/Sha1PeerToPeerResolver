package org.example.sha1PeerToPeer.connections

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.example.sha1PeerToPeer.utils.BaseTest
import kotlin.test.Test

class ListenNodeMessagesUseCaseTest : BaseTest() {
    @MockK
    private lateinit var connectionsHandler: ConnectionsHandler

    @InjectMockKs
    private lateinit var listenNodeMessagesUseCase: ListenNodeMessagesUseCase

    @Test
    fun `invoke calls connectionHandler`() = runTest {
        every {
            connectionsHandler.listenNodesMessages()
        } returns flowOf()

        listenNodeMessagesUseCase.invoke().toList()

        verify(exactly = 1) {
            connectionsHandler.listenNodesMessages()
        }
    }
}
