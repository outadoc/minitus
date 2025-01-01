package fr.outadoc.minitus.display

import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.VideotexBuilder
import fr.outadoc.minitus.GameConstants
import fr.outadoc.minitus.diff.CharacterMatch
import fr.outadoc.minitus.diff.computeDiff
import fr.outadoc.minitus.diff.getHintForNextGuess

internal fun VideotexBuilder.displayGameGrid(
    guesses: List<String>,
    expectedWord: String,
    showExtraLines: Boolean,
) {
    val startPadding = (20 - expectedWord.length) / 2

    withCharacterSize(CharacterSize.DoubleSize) {
        guesses.forEach { guess ->
            appendLine()
            repeatChar(' ', startPadding)

            val diff =
                fr.outadoc.minitus.diff.computeDiff(
                    expectedWord = expectedWord,
                    guess = guess,
                )

            displayDiffLine(diff)
            appendLine()
        }

        if (showExtraLines) {
            if (guesses.size < GameConstants.MAX_ATTEMPTS) {
                appendLine()
                repeatChar(' ', startPadding)

                displayDiffLine(
                    fr.outadoc.minitus.diff.getHintForNextGuess(
                        expectedWord = expectedWord,
                        previousGuesses = guesses,
                    ),
                )
                appendLine()
            }

            (guesses.size + 1 until GameConstants.MAX_ATTEMPTS).forEach { _ ->
                // On affiche des lignes vides pour remplir les guess non-faits
                appendLine()
                repeatChar(' ', startPadding)

                appendLine(
                    expectedWord
                        .map { '.' }
                        .joinToString(separator = ""),
                )
            }
        }
    }

    appendLine()
    appendLine()
}

private fun VideotexBuilder.displayDiffLine(guess: List<CharacterMatch>) {
    guess.forEach { match ->
        when (match) {
            is CharacterMatch.Exact -> {
                withInvertedBackground {
                    withTextColor(Color.Red) {
                        append(match.character)
                    }
                }
            }

            is CharacterMatch.Partial -> {
                withInvertedBackground {
                    withTextColor(Color.Yellow) {
                        append(match.character)
                    }
                }
            }

            is CharacterMatch.None -> {
                append(match.character)
            }
        }
    }
}
