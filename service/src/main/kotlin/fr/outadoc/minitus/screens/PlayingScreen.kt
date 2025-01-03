package fr.outadoc.minitus.screens

import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import fr.outadoc.minitus.GameConstants
import fr.outadoc.minitus.dictionary.pickDailyWord
import fr.outadoc.minitus.display.displayGameGrid
import fr.outadoc.minitus.display.displayLogo
import fr.outadoc.minitus.normalize
import kotlinx.datetime.LocalDate

internal fun playingScreen(
    state: MinitusState.Playing,
    expectedWord: String,
): ServiceResponse<MinitusState> =
    ServiceResponse(
        state = state,
        command =
            ServiceResponse.Command.InputText(
                col = 24,
                line = 24,
                length = expectedWord.length,
            ),
        content =
            buildVideotex {
                clearAll()
                displayLogo()

                displayGameGrid(
                    guesses = state.guesses,
                    expectedWord = expectedWord,
                    showExtraLines = true,
                )

                if (state.lastInputError != null) {
                    withTextColor(Color.Red) {
                        when (state.lastInputError) {
                            MinitusState.Error.InvalidLength -> {
                                appendLine(
                                    "Entrez un mot de ${expectedWord.length} lettres.",
                                )
                            }

                            MinitusState.Error.NotInDictionary -> {
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

internal fun MinitusState.Playing.reduce(
    date: LocalDate,
    userInput: String,
    dictionary: Set<String>,
): MinitusState {
    val inputWord = userInput.normalize()
    if (inputWord.isEmpty()) {
        return copy(
            lastInputError = null,
        )
    }

    if (inputWord.length !in GameConstants.ALLOWED_WORD_LENGTHS) {
        return copy(
            lastInputError = MinitusState.Error.InvalidLength,
        )
    }

    if (inputWord !in dictionary) {
        return copy(
            lastInputError = MinitusState.Error.NotInDictionary,
        )
    }

    val expectedWord = dictionary.pickDailyWord(date)

    if (inputWord == expectedWord) {
        return MinitusState.Win(
            date = date,
            guesses = guesses + inputWord,
        )
    }

    if (guesses.size >= GameConstants.MAX_ATTEMPTS - 1) {
        return MinitusState.Lose(
            date = date,
            guesses = guesses + inputWord,
        )
    }

    return copy(
        guesses = guesses + inputWord,
        lastInputError = null,
    )
}
