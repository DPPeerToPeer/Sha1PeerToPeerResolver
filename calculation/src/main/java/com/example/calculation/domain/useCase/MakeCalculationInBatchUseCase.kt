package com.example.calculation.domain.useCase

import com.example.calculation.IMakeCalculationInBatchUseCase
import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import java.security.MessageDigest
import java.sql.DriverManager.println

internal class MakeCalculationInBatchUseCase: IMakeCalculationInBatchUseCase {
    fun sha1(input: String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-1")
        val digest = md.digest(bytes)

        val result = StringBuilder()
        for (byte in digest) {
            result.append(String.format("%02x", byte))
        }

        return result.toString()
    }

    class FoundException(message: String) : Exception(message)

    class LimitExceededException(message: String) : Exception(message)

    override operator fun invoke(
        batch: Batch,
        hashToFind: String,
    ): CalculationResult {
        val znaki = (97..122).map { it.toChar() } + (65..90).map { it.toChar() } + (48..57).map { it.toChar() }
        var start = batch.start
        val end = batch.end
        var found = false
        var foundedWord = ""
        var actualNewLength = start.length

        fun iterateLastNElements(
            start: String,
            ktoraLiteraOdKonca: Int,
        ) {
            var newElem = ""
            if (ktoraLiteraOdKonca != 0) {
                newElem = start.substring(0, start.length - ktoraLiteraOdKonca)
            } // //pobiera znaki od początku do sprawdzanego znaku

            var indeksNastepnegoZnaku = // //pobiera indeksNastepngoZnaku
                if (start.length - ktoraLiteraOdKonca != start.length) {
                    znaki.indexOf(start[start.length - ktoraLiteraOdKonca])
                } else {
                    znaki.indexOf(start[0])
                }

            for (i in znaki.subList(
                indeksNastepnegoZnaku,
                znaki.size,
            )) { // //Zamienia w petli sprawdzany zank na nastęny
                var text = newElem
                text += i
                if (ktoraLiteraOdKonca > 1) {
                    var remainingChars = start.substring(start.length - ktoraLiteraOdKonca + 1)
                    text += remainingChars
                }
                System.out.println(text)
                var generatedHash = sha1(text)
                if (text == end && generatedHash != hashToFind) {
                    System.out.println("KONIEC ----- limit wyczerpany!!!")
                    System.exit(0)
                    throw LimitExceededException("Limit wyczerpany")
                }
                if (generatedHash == hashToFind) {
                    System.out.println("Znaleziono: $text")
                    foundedWord = text
                    found = true
                    System.exit(0)
                    throw FoundException("Znaleziono: $text")
                }
                if (ktoraLiteraOdKonca - 1 > 0 && !found) {
                    iterateLastNElements(text, ktoraLiteraOdKonca - 1)
                }
            }
        }

        fun checkAllCombinationsOfNLongStartWord(start: String) {
            iterateLastNElements(start, start.length - 1)
            if (!found) {
                for (i in znaki.subList(znaki.indexOf(start[0]) + 1, znaki.size)) {
                    if (start.length - 1 != 0) {
                        var newElem = i + "a".repeat(start.length - 1)
                        iterateLastNElements(newElem, start.length - 1)
                    }
                }
            }
        }
        try {
            checkAllCombinationsOfNLongStartWord(start)
            while (!found) {
                if (start.length < end.length) {
                    val newWord = "a".repeat(actualNewLength + 1)
                    System.out.println("nowe a: $newWord")
                    actualNewLength++
                    checkAllCombinationsOfNLongStartWord(newWord)
                }
            }
        } catch (e: FoundException) {
            return CalculationResult.Found(foundedWord)
        } catch (e: LimitExceededException) {
            return CalculationResult.NotFound
        }
        return CalculationResult.NotFound

        // //DLA BATCHYY
//        var batch_size = 5
//        var batches = mutableListOf<Pair<String, String>>()
//
//        fun iterateLastNElementsForGenerating(
//            start: String,
//            ktoraLiteraOdKonca: Int,
//            end: String,
//        ) {
//            var newElem = start.substring(0, start.length - ktoraLiteraOdKonca)
//            val indeksNastepnegoZnaku =
//                if (start.length - ktoraLiteraOdKonca != start.length) {
//                    znaki.indexOf(start[start.length - ktoraLiteraOdKonca])
//                } else {
//                    znaki.indexOf(start[0])
//                }
//
//            for (i in znaki.subList(indeksNastepnegoZnaku, znaki.size)) {
//                var text = newElem + i
//                if (ktoraLiteraOdKonca >= 2) {
//                    val remainingChars = start.substring(start.length - ktoraLiteraOdKonca + 1)
// //                    newElem += remainingChars
//                    text += remainingChars
//                }
//
//                batches.add(Pair(text + "aaaa", text + "9999"))
//                println(Pair(text + "aaaa", text + "9999"))
//
//                if (text == end) {
//                    println("KONIEC ----- limit wyczerpany!!!")
//                }
//
//                if (ktoraLiteraOdKonca - 1 > 0) {
//                    iterateLastNElementsForGenerating(text, ktoraLiteraOdKonca - 1, end)
//                }
//            }
//        }
//
//        for (i in batch_size until 11) {
//            if (i == batch_size) {
//                println("POCZĄTEK: a     KONIEC: " + "9".repeat(batch_size - 1))
//                batches.add(Pair("a", "9".repeat(batch_size - 1)))
//                println(Pair("a", "9".repeat(batch_size - 1)))
//            } else {
//                iterateLastNElementsForGenerating(
//                    "a".repeat(i - batch_size),
//                    i - batch_size,
//                    "9".repeat(i - batch_size),
//                )
//            }
//        }
//
//        return CalculationResult.NotFound
//    }
    }
}
