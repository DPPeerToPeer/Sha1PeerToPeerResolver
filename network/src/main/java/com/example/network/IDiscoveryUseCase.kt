package com.example.network

import com.example.common.models.Node

interface IDiscoveryUseCase {
    suspend operator fun invoke(
        myPort: Int,
        hashToFind: String,
    ): List<Node>
}
