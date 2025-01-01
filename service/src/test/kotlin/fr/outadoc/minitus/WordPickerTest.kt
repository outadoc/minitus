package fr.outadoc.minitus

import fr.outadoc.minitus.dictionary.pickDailyWord
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class WordPickerTest {
    @Test
    fun `Consecutive calls to pickDailyWord give the same result`() {
        val today = LocalDate(2024, 12, 31)
        val words = setOf("1", "2", "3")

        val firstPick = words.pickDailyWord(today)

        repeat(10) {
            assertEquals(firstPick, words.pickDailyWord(today))
        }
    }

    @Test
    fun `When I change the date, pickDailyWord gives a different result`() {
        val today = LocalDate(2024, 12, 31)
        val words = setOf("1", "2", "3")

        val firstPick = words.pickDailyWord(today)
        val secondPick = words.pickDailyWord(today + DatePeriod(days = 1))

        assertNotEquals(firstPick, secondPick)
    }

    @Test
    fun `When I run out of items, we start over`() {
        val today = LocalDate(2024, 12, 31)
        val words = setOf("1", "2", "3")

        val firstPick = words.pickDailyWord(today)
        val secondPick = words.pickDailyWord(today + DatePeriod(days = 3))

        assertEquals(firstPick, secondPick)
    }
}
