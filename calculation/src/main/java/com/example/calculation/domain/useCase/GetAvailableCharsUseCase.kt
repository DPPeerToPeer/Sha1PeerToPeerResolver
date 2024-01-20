package com.example.calculation.domain.useCase

internal class GetAvailableCharsUseCase {

    operator fun invoke(): List<Char> =
        (97..122).map { it.toChar() } + (65..90).map { it.toChar() } + (48..57).map { it.toChar() }
}
