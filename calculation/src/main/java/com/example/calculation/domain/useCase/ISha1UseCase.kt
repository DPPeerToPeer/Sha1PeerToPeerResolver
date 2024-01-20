package com.example.calculation.domain.useCase

internal interface ISha1UseCase {
    operator fun invoke(text: String): String
}
