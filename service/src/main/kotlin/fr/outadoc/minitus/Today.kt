package fr.outadoc.minitus

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime

fun today(): LocalDate {
    return GameConstants.clock.now().toLocalDateTime(GameConstants.tz).date
}
