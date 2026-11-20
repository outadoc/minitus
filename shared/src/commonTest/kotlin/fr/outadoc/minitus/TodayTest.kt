package fr.outadoc.minitus

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TodayTest {
    @Test
    fun `Today in Paris is at most one day away from today in UTC`() {
        val utcToday = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val today = today()

        assertTrue(
            today in (utcToday - DatePeriod(days = 1))..(utcToday + DatePeriod(days = 1)),
            "today() = $today, UTC date = $utcToday",
        )
    }

    @Test
    fun `The game time zone follows daylight saving time`() {
        loadTimeZoneDatabase()
        val tz = TimeZone.of(GameConstants.TIME_ZONE)

        assertEquals(
            LocalDateTime(2025, 1, 15, 23, 30),
            Instant.parse("2025-01-15T22:30:00Z").toLocalDateTime(tz),
        )
        assertEquals(
            LocalDateTime(2025, 7, 16, 0, 30),
            Instant.parse("2025-07-15T22:30:00Z").toLocalDateTime(tz),
        )
    }
}
