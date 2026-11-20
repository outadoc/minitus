package fr.outadoc.minitus

/**
 * Décompose la chaîne de caractères en forme normale NFD,
 * c'est-à-dire en séparant les caractères de base de leurs diacritiques.
 */
internal expect fun String.toNfd(): String

// Bloc Unicode « Combining Diacritical Marks »
private val COMBINING_DIACRITICAL_MARKS = '̀'..'ͯ'

internal fun String.normalize(): String {
    // On normalise la chaîne de caractères en NFD,
    // c'est-à-dire en décomposant les caractères en base et diacritiques
    return toNfd()
        .filter { char ->
            // On supprime toutes les diacritiques
            char !in COMBINING_DIACRITICAL_MARKS
        }.uppercase()
}
