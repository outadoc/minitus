package fr.outadoc.minitus

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone

internal object GameConstants {
    const val MAX_ATTEMPTS = 6

    // On ne veut que des mots en majuscules de 5 à 10 caractères
    val ALLOWED_WORD_REGEX = Regex("^[A-Z]{5,10}$")

    val clock = Clock.System
    val tz = TimeZone.of("Europe/Paris")
}
