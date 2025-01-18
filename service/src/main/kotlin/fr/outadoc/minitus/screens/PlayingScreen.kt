package fr.outadoc.minitus.screens

import fr.outadoc.minipavi.core.model.FunctionKey
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import fr.outadoc.minitus.GameConstants
import fr.outadoc.minitus.dictionary.pickDailyWord
import fr.outadoc.minitus.display.displayFooter
import fr.outadoc.minitus.display.displayGameGrid
import fr.outadoc.minitus.display.displayHeader
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
                col = (40 - expectedWord.length) / 2,
                line = 20,
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
                displayHeader(
                    puzzleNumber = state.puzzleNumber,
                )

                displayGameGrid(
                    guesses = state.guesses,
                    expectedWord = expectedWord,
                    showExtraLines = true,
                )

                repeatChar(' ', 9)
                append("Entrez un mot + ")
                withInvertedBackground {
                    appendLine("ENVOI")
                }

                appendLine()
                appendLine()

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

                            MinitusState.Playing.Error.InvalidFirstLetter -> {
                                appendLine("Le mot doit commencer par ${expectedWord.first()}.")
                            }

                            MinitusState.Playing.Error.AlreadyPlayed -> {
                                appendLine("Ce mot a déjà été joué.")
                            }
                        }
                    }
                }

                displayFooter()
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

    if (inputWord.first() != expectedWord.first()) {
        return copy(
            lastInputError = MinitusState.Playing.Error.InvalidFirstLetter,
        )
    }

    if (inputWord in guesses) {
        return copy(
            lastInputError = MinitusState.Playing.Error.AlreadyPlayed,
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
