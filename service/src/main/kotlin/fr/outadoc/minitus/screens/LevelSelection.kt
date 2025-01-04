package fr.outadoc.minitus.screens

import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import fr.outadoc.minitus.dictionary.getPuzzleNumber
import fr.outadoc.minitus.display.displayHeader
import fr.outadoc.minitus.today

fun levelSelectionScreen(state: MinitusState.LevelSelection): ServiceResponse<MinitusState> {
    val latestPuzzle: Int = today().getPuzzleNumber()

    return ServiceResponse(
        command =
            ServiceResponse.Command.InputText(
                col = 1,
                line = 11,
                length = latestPuzzle.toString().length,
            ),
        state = state,
        content =
            buildVideotex {
                clearAll()
                displayHeader(puzzleNumber = null)

                appendLine("Sélection de niveau")
                appendLine()
                appendLine("Niveaux disponibles :")
                appendLine("1 à $latestPuzzle")
                appendLine()
                appendLine("Entrez un niveau :")

                appendLine()
                appendLine()

                if (state.error != null) {
                    withTextColor(Color.Red) {
                        when (state.error) {
                            MinitusState.LevelSelection.Error.InvalidLevelFormat -> {
                                appendLine("Niveau invalide.")
                                appendLine("Veuillez entrer un nombre.")
                            }

                            MinitusState.LevelSelection.Error.InvalidLevelRange -> {
                                appendLine("Ce niveau n'est pas encore disponible.")
                            }
                        }
                    }
                }
            },
    )
}

internal fun MinitusState.LevelSelection.reduce(userInput: String): MinitusState {
    val inputLevel = userInput.toIntOrNull()
    val latestPuzzle: Int = today().getPuzzleNumber()

    if (inputLevel == null) {
        return copy(
            error = MinitusState.LevelSelection.Error.InvalidLevelFormat,
        )
    }

    if (inputLevel !in 1..latestPuzzle) {
        return copy(
            error = MinitusState.LevelSelection.Error.InvalidLevelRange,
        )
    }

    return MinitusState.Playing(
        puzzleNumber = inputLevel,
        guesses = emptyList(),
    )
}
