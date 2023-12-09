package com.example.common.models

import com.example.common.IGetMyIdUseCase
import java.util.UUID

internal class GetMyIdUseCase : IGetMyIdUseCase {

    private val id by lazy {
        NodeId(UUID.randomUUID().toString())
    }

    override suspend operator fun invoke(): NodeId {
        return id
    }
}
