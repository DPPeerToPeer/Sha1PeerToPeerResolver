package com.example.calculation.domain.useCase

import com.example.common.models.Batch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

internal class CreateBatchesUseCase(
    private val getAvailableCharsUseCase: GetAvailableCharsUseCase,
    private val scope: CoroutineScope,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): ReceiveChannel<Batch> =
        scope.produce {
            val lengthOfIteratedCharsInWord = 5
            val chars = getAvailableCharsUseCase()

            suspend fun iterateLastNElementsForGenerating(
                start: String,
                lastNLetter: Int,
                end: String,
            ) {
                val newElem = start.substring(0, start.length - lastNLetter)
                val nextCharIndex =
                    if (start.length - lastNLetter != start.length) {
                        chars.indexOf(start[start.length - lastNLetter])
                    } else {
                        chars.indexOf(start[0])
                    }

                for (i in chars.subList(nextCharIndex, chars.size)) {
                    var text = newElem + i
                    if (lastNLetter >= 2) {
                        val remainingChars = start.substring(start.length - lastNLetter + 1)
                        text += remainingChars
                    }

                    send(
                        Batch(
                            start = text + "aaaa",
                            end = text + "9999",
                        ),
                    )

                    if (text == end) {
                        println("END ----- limit exhausted!!")
                    }

                    if (lastNLetter - 1 > 0) {
                        iterateLastNElementsForGenerating(text, lastNLetter - 1, end)
                    }
                }
            }

            for (i in lengthOfIteratedCharsInWord until 11) {
                if (i == lengthOfIteratedCharsInWord) {
                    println("START: a     END: " + "9".repeat(lengthOfIteratedCharsInWord - 1))
                    send(
                        Batch(
                            start = "a",
                            end = "9".repeat(lengthOfIteratedCharsInWord - 1),
                        ),
                    )
                } else {
                    iterateLastNElementsForGenerating(
                        "a".repeat(i - lengthOfIteratedCharsInWord),
                        i - lengthOfIteratedCharsInWord,
                        "9".repeat(i - lengthOfIteratedCharsInWord),
                    )
                }
            }
        }
}
