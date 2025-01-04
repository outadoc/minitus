package fr.outadoc.minitus.screens

import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import fr.outadoc.minitus.dictionary.pickDailyWord
import fr.outadoc.minitus.display.displayGameGrid
import fr.outadoc.minitus.display.displayLogo

internal fun winScreen(
    state: MinitusState.Win,
    dictionary: Set<String>,
): ServiceResponse<MinitusState> {
    val expectedWord = dictionary.pickDailyWord(state.puzzleNumber)
    return ServiceResponse(
        state = state,
        content =
            buildVideotex {
                clearAll()
                displayLogo(
                    puzzleNumber = state.puzzleNumber,
                )

                displayGameGrid(
                    guesses = state.guesses,
                    expectedWord = expectedWord,
                    showExtraLines = false,
                )

                withTextColor(Color.Green) {
                    appendLine("C'est gagné !")
                }
            },
    )
}
