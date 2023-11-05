package org.example.sha1PeerToPeer.domain.useCases

class GetCurrentTimeUseCase {

    operator fun invoke(): Long = System.currentTimeMillis()
}
