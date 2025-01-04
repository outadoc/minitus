package fr.outadoc.minitus.screens

import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.core.model.ServiceResponse.Command.FunctionKey
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import fr.outadoc.minitus.GameConstants
import fr.outadoc.minitus.dictionary.pickDailyWord
import fr.outadoc.minitus.display.displayGameGrid
import fr.outadoc.minitus.display.displayLogo
import fr.outadoc.minitus.normalize

internal fun playingScreen(
    state: MinitusState.Playing,
    dictionary: Set<String>,
): ServiceResponse<MinitusState> {
    val expectedWord = dictionary.pickDailyWord(state.puzzleNumber)
    return ServiceResponse(
        state = state,
        command =
            ServiceResponse.Command.InputText(
                col = 24,
                line = 24,
                length = expectedWord.length,
                submitWith =
                    setOf(
                        FunctionKey.Envoi,
                        FunctionKey.Sommaire,
                    ),
            ),
        content =
            buildVideotex {
                clearAll()
                displayLogo(
                    puzzleNumber = state.puzzleNumber,
                )

                displayGameGrid(
                    guesses = state.guesses,
                    expectedWord = expectedWord,
                    showExtraLines = true,
                )

                if (state.lastInputError != null) {
                    withTextColor(Color.Red) {
                        when (state.lastInputError) {
                            MinitusState.Playing.Error.InvalidLength -> {
                                appendLine(
                                    "Entrez un mot de ${expectedWord.length} lettres.",
                                )
                            }

                            MinitusState.Playing.Error.NotInDictionary -> {
                                appendLine("Je ne connais pas ce mot.")
                            }
                        }
                    }
                }

                moveCursorTo(1, 24)
                append("Entrez un mot + ")
                withInvertedBackground {
                    appendLine("ENVOI")
                }
            },
    )
}

internal fun MinitusState.Playing.reduce(
    puzzleNumber: Int,
    userInput: String,
    dictionary: Set<String>,
): MinitusState {
    val inputWord = userInput.normalize()
    if (inputWord.isEmpty()) {
        return copy(
            lastInputError = null,
        )
    }

    val expectedWord = dictionary.pickDailyWord(puzzleNumber)

    if (inputWord.length != expectedWord.length) {
        return copy(
            lastInputError = MinitusState.Playing.Error.InvalidLength,
        )
    }

    if (inputWord !in dictionary) {
        return copy(
            lastInputError = MinitusState.Playing.Error.NotInDictionary,
        )
    }

    if (inputWord == expectedWord) {
        return MinitusState.Win(
            puzzleNumber = puzzleNumber,
            guesses = guesses + inputWord,
        )
    }

    if (guesses.size >= GameConstants.MAX_ATTEMPTS - 1) {
        return MinitusState.Lose(
            puzzleNumber = puzzleNumber,
            guesses = guesses + inputWord,
        )
    }

    return copy(
        guesses = guesses + inputWord,
        lastInputError = null,
    )
}
