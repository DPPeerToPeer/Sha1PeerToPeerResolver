package com.example.network.internal

import com.example.network.internal.data.nodes.ConnectionsHandler
import com.example.network.models.Port
import com.example.network.utils.BaseTest
import com.example.socketsFacade.IReadWriteSocket
import com.example.socketsFacade.IServerSocket
import com.example.socketsFacade.IServerSocketFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class ConnectionHandlerTest : BaseTest() {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())

    @MockK
    private lateinit var serverSocketFactory: IServerSocketFactory

    @InjectMockKs
    private lateinit var connectionsHandler: ConnectionsHandler

    @Test
    fun `runAndReturnPort return port from serverSocket`() = runTest {
        val acceptChannel = Channel<IReadWriteSocket>(Channel.UNLIMITED)
        mockServerSocket(
            port = 6,
            acceptChannel = acceptChannel,
        )

        val port = connectionsHandler.runAndReturnPort()

        port shouldBe Port(port = 6)

        coVerify(exactly = 1) {
            serverSocketFactory.create()
        }
    }

    private fun mockServerSocket(
        port: Int,
        acceptChannel: Channel<IReadWriteSocket>,
    ): IServerSocket {
        val serverSocketMock = mockk<IServerSocket>()

        every {
            serverSocketFactory.create()
        } returns serverSocketMock
        coEvery {
            serverSocketMock.port
        } returns port
        coEvery {
            serverSocketMock.accept()
        } coAnswers {
            acceptChannel.receive()
        }

        return serverSocketMock
    }
}
