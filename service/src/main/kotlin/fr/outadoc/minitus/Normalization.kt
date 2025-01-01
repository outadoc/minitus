package fr.outadoc.minitus

import java.lang.Character.UnicodeBlock
import java.text.Normalizer

internal fun String.normalize(): String {
    // On normalise la chaîne de caractères en NFD,
    // c'est-à-dire en décomposant les caractères en base et diacritiques
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .filter { char ->
            // On supprime toutes les diacritiques
            UnicodeBlock.of(char) != UnicodeBlock.COMBINING_DIACRITICAL_MARKS
        }
        .uppercase()
}
