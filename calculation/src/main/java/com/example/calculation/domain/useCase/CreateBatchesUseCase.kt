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
            val batchSize = 5
            val znaki = getAvailableCharsUseCase()

            suspend fun iterateLastNElementsForGenerating(
                start: String,
                ktoraLiteraOdKonca: Int,
                end: String,
            ) {
                val newElem = start.substring(0, start.length - ktoraLiteraOdKonca)
                val indeksNastepnegoZnaku =
                    if (start.length - ktoraLiteraOdKonca != start.length) {
                        znaki.indexOf(start[start.length - ktoraLiteraOdKonca])
                    } else {
                        znaki.indexOf(start[0])
                    }

                for (i in znaki.subList(indeksNastepnegoZnaku, znaki.size)) {
                    var text = newElem + i
                    if (ktoraLiteraOdKonca >= 2) {
                        val remainingChars = start.substring(start.length - ktoraLiteraOdKonca + 1)
                        text += remainingChars
                    }

                    send(
                        Batch(
                            start = text + "aaaa",
                            end = text + "9999",
                        ),
                    )

                    if (text == end) {
                        println("KONIEC ----- limit wyczerpany!!!")
                    }

                    if (ktoraLiteraOdKonca - 1 > 0) {
                        iterateLastNElementsForGenerating(text, ktoraLiteraOdKonca - 1, end)
                    }
                }
            }

            for (i in batchSize until 11) {
                if (i == batchSize) {
                    println("POCZĄTEK: a     KONIEC: " + "9".repeat(batchSize - 1))
                    send(
                        Batch(
                            start = "a",
                            end = "9".repeat(batchSize - 1),
                        ),
                    )
                } else {
                    iterateLastNElementsForGenerating(
                        "a".repeat(i - batchSize),
                        i - batchSize,
                        "9".repeat(i - batchSize),
                    )
                }
            }
        }
}
