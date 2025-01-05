package fr.outadoc.minitus

import fr.outadoc.minitus.dictionary.pickDailyWord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class WordPickerTest {
    @Test
    fun `Consecutive calls to pickDailyWord give the same result`() {
        val puzzleNumber = 3
        val words = setOf("1", "2", "3")

        val firstPick = words.pickDailyWord(puzzleNumber)

        repeat(10) {
            assertEquals(firstPick, words.pickDailyWord(puzzleNumber))
        }
    }

    @Test
    fun `When I change the date, pickDailyWord gives a different result`() {
        val puzzleNumber = 3
        val words = setOf("1", "2", "3")

        val firstPick = words.pickDailyWord(puzzleNumber)
        val secondPick = words.pickDailyWord(puzzleNumber + 1)

        assertNotEquals(firstPick, secondPick)
    }

    @Test
    fun `When I run out of items, we start over`() {
        val puzzleNumber = 3
        val words = setOf("1", "2", "3")

        val firstPick = words.pickDailyWord(puzzleNumber)
        val secondPick = words.pickDailyWord(puzzleNumber + 3)

        assertEquals(firstPick, secondPick)
    }
}
