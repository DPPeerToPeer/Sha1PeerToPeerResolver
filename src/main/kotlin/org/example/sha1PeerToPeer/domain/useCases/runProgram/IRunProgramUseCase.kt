package org.example.sha1PeerToPeer.domain.useCases.runProgram

interface IRunProgramUseCase {

    suspend operator fun invoke(hashToFind: String)
}
