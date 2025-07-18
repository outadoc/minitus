package fr.outadoc.minitus

import fr.outadoc.minitus.dictionary.getPuzzleNumber
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class PuzzlePickerTest {
    @Test
    fun `Consecutive calls to getPuzzleNumber give the same result`() {
        val today = LocalDate(2024, 12, 31)

        val firstPick = today.getPuzzleNumber()

        repeat(10) {
            assertEquals(firstPick, today.getPuzzleNumber())
        }
    }

    @Test
    fun `When I change the date, getPuzzleNumber gives a different result`() {
        val today = LocalDate(2024, 12, 31)

        val firstPick = today.getPuzzleNumber()
        val secondPick = (today + DatePeriod(days = 1)).getPuzzleNumber()

        assertNotEquals(firstPick, secondPick)
    }

    @Test
    fun `Consecutive days get a consecutive puzzle number`() {
        val start = LocalDate(2024, 12, 31)

        (0..10).forEach { index ->
            val date = start + DatePeriod(days = index)
            val expectedPuzzleNumber = start.getPuzzleNumber() + index

            assertEquals(
                expected = expectedPuzzleNumber,
                actual = date.getPuzzleNumber(),
            )
        }
    }

    @Test
    fun `Days before the epoch throw an exception`() {
        val date = LocalDate(2024, 12, 30)

        assertFailsWith<IllegalArgumentException> {
            date.getPuzzleNumber()
        }
    }

    @Test
    fun `On a known date, the puzzle number is known`() {
        val today = LocalDate(2025, 7, 18)

        assertEquals(
            expected = 199,
            actual = today.getPuzzleNumber()
        )
    }
}
