package fr.outadoc.minitus.diff

sealed interface CharacterMatch {
    val character: Char

    data class Exact(override val character: Char) : CharacterMatch

    data class Partial(override val character: Char) : CharacterMatch

    data class None(override val character: Char) : CharacterMatch
}
