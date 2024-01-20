package com.example.calculation.domain.useCase

internal class FakeSha1UseCase : ISha1UseCase {

    var currentCorrect: String = ""

    override fun invoke(text: String): String {
        return if (text == currentCorrect) {
            return "hash"
        } else {
            "other"
        }
    }
}
