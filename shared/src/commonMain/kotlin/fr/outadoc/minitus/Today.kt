package fr.outadoc.minitus

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Charge la base de données des fuseaux horaires, si la plateforme en a besoin.
 */
internal expect fun loadTimeZoneDatabase()

private val tz: TimeZone by lazy {
    loadTimeZoneDatabase()
    TimeZone.of(GameConstants.TIME_ZONE)
}

/**
 * Renvoie la date du jour dans le fuseau horaire du jeu.
 */
fun today(): LocalDate = Clock.System.now().toLocalDateTime(tz).date
