package com.example.common

import com.example.common.models.NodeId

interface IGetMyIdUseCase {

    suspend operator fun invoke(): NodeId
}
