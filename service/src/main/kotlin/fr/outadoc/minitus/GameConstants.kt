package fr.outadoc.minitus

internal object GameConstants {
    const val MAX_ATTEMPTS = 6

    // On ne veut que des mots en majuscules de 5 à 10 caractères
    val ALLOWED_WORD_LENGTHS = 5..10
    val ALLOWED_WORD_REGEX = Regex("^[A-Z]{5,10}$")
}
