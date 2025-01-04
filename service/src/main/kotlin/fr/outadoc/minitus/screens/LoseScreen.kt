package fr.outadoc.minitus.screens

import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import fr.outadoc.minitus.dictionary.pickDailyWord
import fr.outadoc.minitus.display.displayFooter
import fr.outadoc.minitus.display.displayGameGrid
import fr.outadoc.minitus.display.displayHeader

internal fun loseScreen(
    state: MinitusState.Lose,
    dictionary: Set<String>,
): ServiceResponse<MinitusState> {
    val expectedWord = dictionary.pickDailyWord(state.puzzleNumber)
    return ServiceResponse(
        state = state,
        content =
            buildVideotex {
                clearAll()
                displayHeader(
                    puzzleNumber = state.puzzleNumber,
                )

                displayGameGrid(
                    guesses = state.guesses,
                    expectedWord = expectedWord,
                    showExtraLines = false,
                )

                withTextColor(Color.Red) {
                    withInvertedBackground {
                        withCharacterSize(CharacterSize.DoubleHeight) {
                            repeatChar(' ', 17)
                            append("Perdu :(")
                            repeatChar(' ', 15)
                        }
                    }

                    appendLine()

                    append("Le mot était ")
                    withInvertedBackground {
                        appendLine(expectedWord)
                    }
                }

                displayFooter()
            },
    )
}
