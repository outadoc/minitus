package fr.outadoc.minitus

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val tz = TimeZone.of(GameConstants.TIME_ZONE)

actual fun today(): LocalDate = Clock.System.now().toLocalDateTime(tz).date
