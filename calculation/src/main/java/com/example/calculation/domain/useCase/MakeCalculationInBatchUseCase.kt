package com.example.calculation.domain.useCase

import com.example.calculation.IMakeCalculationInBatchUseCase
import com.example.common.models.Batch
import com.example.common.models.CalculationResult

internal class MakeCalculationInBatchUseCase(
    private val sha1UseCase: Sha1UseCase,
    private val getAvailableCharsUseCase: GetAvailableCharsUseCase,
) : IMakeCalculationInBatchUseCase {

    class FoundException(message: String) : Exception(message)

    class LimitExceededException(message: String) : Exception(message)

    override operator fun invoke(
        batch: Batch,
        hashToFind: String,
    ): CalculationResult {
        println("Invoking")

        val znaki = getAvailableCharsUseCase()
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
                // println(text)
                var generatedHash = sha1UseCase(text = text)
                if (text == end && generatedHash != hashToFind) {
                    println("KONIEC ----- limit wyczerpany!!!")
                    throw LimitExceededException("Limit wyczerpany")
                }
                if (generatedHash == hashToFind) {
                    println("Znaleziono: $text")
                    foundedWord = text
                    found = true
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
                    println("nowe a: $newWord")
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
    }
}
