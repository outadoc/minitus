package fr.outadoc.minitus.diff

internal fun computeDiff(
    expectedWord: String,
    guess: String,
): List<CharacterMatch> {
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
                CharacterMatch.Exact(actual)
            }

            availableLetters.contains(actual) -> {
                availableLetters.remove(actual)
                CharacterMatch.Partial(actual)
            }

            else -> {
                CharacterMatch.None(actual)
            }
        }
    }
}

internal fun getHintForNextGuess(
    expectedWord: String,
    previousGuesses: List<String>,
): List<CharacterMatch> {
    if (previousGuesses.isEmpty()) {
        return expectedWord.mapIndexed { index, expectedChar ->
            if (index == 0) {
                CharacterMatch.Exact(expectedWord.first())
            } else {
                CharacterMatch.None('.')
            }
        }
    }

    return expectedWord.mapIndexed { index, expectedChar ->
        if (previousGuesses.any { previousGuess -> previousGuess[index] == expectedChar }) {
            CharacterMatch.Exact(expectedChar)
        } else {
            CharacterMatch.None('.')
        }
    }
}
