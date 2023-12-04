package com.example.nodes.domain.useCase

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


class RemoveNotActiveNodesUseCase {
    private val time: Duration = 1.minutes
    suspend operator fun invoke() {
        TODO() //przechodzi po kolekcji i sprawdza czy czas od ostatniego pojawienia sie node jest większy od time.
            // Jeśli tak usówa node z kolekcji.
    }
}
