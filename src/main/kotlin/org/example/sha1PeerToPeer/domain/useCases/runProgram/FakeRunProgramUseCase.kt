package org.example.sha1PeerToPeer.domain.useCases.runProgram

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class FakeRunProgramUseCase : IRunProgramUseCase {

    override suspend fun invoke(hashToFind: String) {
        delay(3.seconds)
    }
}
