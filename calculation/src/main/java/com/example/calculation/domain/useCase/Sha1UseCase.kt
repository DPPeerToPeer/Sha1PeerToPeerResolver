package com.example.calculation.domain.useCase

import java.security.MessageDigest

internal class Sha1UseCase {

    operator fun invoke(text: String): String {
        val bytes = text.toByteArray()
        val md = MessageDigest.getInstance("SHA-1")
        val digest = md.digest(bytes)

        val result = StringBuilder()
        for (byte in digest) {
            result.append(String.format("%02x", byte))
        }

        return result.toString()
    }
}
