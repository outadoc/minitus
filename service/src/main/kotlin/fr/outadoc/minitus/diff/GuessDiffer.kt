package fr.outadoc.minitus.diff

internal fun computeDiff(
    expectedWord: String,
    guess: String,
): List<fr.outadoc.minitus.diff.CharacterMatch> {
    val zipped = expectedWord.zip(guess)

    // On garde une copie modifiable des lettres disponibles
    val availableLetters: MutableList<Char> = expectedWord.toMutableList()

    // Les lettres avec un match exact ne peuvent pas être utilisées pour un match partiel,
    // donc on les retire de la liste des lettres disponibles.
    zipped.forEach { (expected, actual) ->
        if (expected == actual) {
            availableLetters.remove(actual)
        }
    }

    // On compare les lettres restantes pour trouver les matchs partiels
    return zipped.map { (expected, actual) ->
        when {
            expected == actual -> {
                fr.outadoc.minitus.diff.CharacterMatch.Exact(actual)
            }

            availableLetters.contains(actual) -> {
                availableLetters.remove(actual)
                fr.outadoc.minitus.diff.CharacterMatch.Partial(actual)
            }

            else -> {
                fr.outadoc.minitus.diff.CharacterMatch.None(actual)
            }
        }
    }
}

internal fun getHintForNextGuess(
    expectedWord: String,
    previousGuesses: List<String>,
): List<fr.outadoc.minitus.diff.CharacterMatch> {
    if (previousGuesses.isEmpty()) {
        return expectedWord.mapIndexed { index, expectedChar ->
            if (index == 0) {
                fr.outadoc.minitus.diff.CharacterMatch.Exact(expectedWord.first())
            } else {
                fr.outadoc.minitus.diff.CharacterMatch.None('.')
            }
        }
    }

    return expectedWord.mapIndexed { index, expectedChar ->
        if (previousGuesses.any { previousGuess -> previousGuess[index] == expectedChar }) {
            fr.outadoc.minitus.diff.CharacterMatch.Exact(expectedChar)
        } else {
            fr.outadoc.minitus.diff.CharacterMatch.None('.')
        }
    }
}
