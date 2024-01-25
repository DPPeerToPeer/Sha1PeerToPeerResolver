package com.example.calculation.domain.useCase

import com.example.calculation.IMakeCalculationInBatchUseCase
import com.example.common.models.Batch
import com.example.common.models.CalculationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

internal class MakeCalculationInBatchUseCase(
    private val sha1UseCase: ISha1UseCase,
    private val getAvailableCharsUseCase: GetAvailableCharsUseCase,
) : IMakeCalculationInBatchUseCase {
    class FoundException(message: String) : Exception(message)

    class LimitExceededException(message: String) : Exception(message)

    override suspend operator fun invoke(
        batch: Batch,
        hashToFind: String,
    ): CalculationResult =
        withContext(Dispatchers.Default) {
            println("Started calculation for $batch")

            val chars = getAvailableCharsUseCase()
            var start = batch.start
            val end = batch.end
            var found = false
            var foundWord = ""
            var actualNewLength = start.length

            fun iterateLastNElements(
                start: String,
                lastNLetter: Int,
            ) {
                var newElem = ""
                if (lastNLetter != 0) {
                    newElem = start.substring(0, start.length - lastNLetter)
                }

                var nextCharIndex =
                    if (start.length - lastNLetter != start.length) {
                        chars.indexOf(start[start.length - lastNLetter])
                    } else {
                        chars.indexOf(start[0])
                    }

                for (i in chars.subList(
                    nextCharIndex,
                    chars.size,
                )) {
                    var text = newElem
                    text += i
                    if (lastNLetter > 1) {
                        val remainingChars = start.substring(start.length - lastNLetter + 1)
                        text += remainingChars
                    }
                    ensureActive()
                    val generatedHash = sha1UseCase(text = text)
                    if (text == end && generatedHash != hashToFind) {
                        println("END ----- limit exhausted!!!")
                        throw LimitExceededException("limit exhausted")
                    }
                    if (generatedHash == hashToFind) {
                        println("Found: $text")
                        foundWord = text
                        found = true
                        throw FoundException("Found: $text")
                    }
                    if (lastNLetter - 1 > 0 && !found) {
                        iterateLastNElements(text, lastNLetter - 1)
                    }
                }
            }

            fun checkAllCombinationsOfNLongStartWord(start: String) {
                iterateLastNElements(start, start.length - 1)
                if (!found) {
                    for (i in chars.subList(chars.indexOf(start[0]) + 1, chars.size)) {
                        if (start.length - 1 != 0) {
                            val newElem = i + "a".repeat(start.length - 1)
                            ensureActive()
                            iterateLastNElements(newElem, start.length - 1)
                        }
                    }
                }
            }
            try {
                checkAllCombinationsOfNLongStartWord(start)
                while (!found) {
                    ensureActive()
                    if (start.length < end.length) {
                        val newWord = "a".repeat(actualNewLength + 1)
                        println("NEW a: $newWord")
                        actualNewLength++
                        checkAllCombinationsOfNLongStartWord(newWord)
                    }
                }
            } catch (e: FoundException) {
                return@withContext CalculationResult.Found(foundWord)
            } catch (e: LimitExceededException) {
                return@withContext CalculationResult.NotFound
            }
            return@withContext CalculationResult.NotFound
        }
}
