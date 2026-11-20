package fr.outadoc.minitus

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class TodayTest {
    @Test
    fun `In winter, the day changes at 23h UTC`() {
        assertEquals(LocalDate(2025, 1, 15), FixedClock("2025-01-15T22:59:59Z").today())
        assertEquals(LocalDate(2025, 1, 16), FixedClock("2025-01-15T23:00:00Z").today())
    }

    @Test
    fun `In summer, the day changes at 22h UTC`() {
        assertEquals(LocalDate(2025, 7, 15), FixedClock("2025-07-15T21:59:59Z").today())
        assertEquals(LocalDate(2025, 7, 16), FixedClock("2025-07-15T22:00:00Z").today())
    }
}
