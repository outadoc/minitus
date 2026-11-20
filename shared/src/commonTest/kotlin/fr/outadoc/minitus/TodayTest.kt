package fr.outadoc.minitus

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
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
}
