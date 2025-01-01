package fr.outadoc.minitus

import fr.outadoc.minitus.diff.CharacterMatch
import fr.outadoc.minitus.diff.computeDiff
import kotlin.test.Test
import kotlin.test.assertEquals

class GuessDifferTest {
    @Test
    fun `Given we expect LAPIN, then with input LAPIN, we get a full match`() {
        val expected = "LAPIN"
        val guess = "LAPIN"

        val result =
            computeDiff(
                expectedWord = expected,
                guess = guess,
            )

        assertEquals(
            expected =
                listOf(
                    CharacterMatch.Exact('L'),
                    CharacterMatch.Exact('A'),
                    CharacterMatch.Exact('P'),
                    CharacterMatch.Exact('I'),
                    CharacterMatch.Exact('N'),
                ),
            actual = result,
        )
    }

    @Test
    fun `Given we expect LAPIN, then with input ROUGE, we get no match`() {
        val expected = "LAPIN"
        val guess = "ROUGE"

        val result =
            computeDiff(
                expectedWord = expected,
                guess = guess,
            )

        assertEquals(
            expected =
                listOf(
                    CharacterMatch.None('R'),
                    CharacterMatch.None('O'),
                    CharacterMatch.None('U'),
                    CharacterMatch.None('G'),
                    CharacterMatch.None('E'),
                ),
            actual = result,
        )
    }

    @Test
    fun `Given we expect LAPIN, then with input LATIN, we get a partial match`() {
        val expected = "LAPIN"
        val guess = "LATIN"

        val result =
            computeDiff(
                expectedWord = expected,
                guess = guess,
            )

        assertEquals(
            expected =
                listOf(
                    CharacterMatch.Exact('L'),
                    CharacterMatch.Exact('A'),
                    CharacterMatch.None('T'),
                    CharacterMatch.Exact('I'),
                    CharacterMatch.Exact('N'),
                ),
            actual = result,
        )
    }

    @Test
    fun `Given we expect LAPIN, then with input NIPAL, we get a partial match`() {
        val expected = "LAPIN"
        val guess = "NIPAL"

        val result =
            computeDiff(
                expectedWord = expected,
                guess = guess,
            )

        assertEquals(
            expected =
                listOf(
                    CharacterMatch.Partial('N'),
                    CharacterMatch.Partial('I'),
                    CharacterMatch.Exact('P'),
                    CharacterMatch.Partial('A'),
                    CharacterMatch.Partial('L'),
                ),
            actual = result,
        )
    }

    @Test
    fun `Given we expect CHEVAL, then with input ECLATER, we get a partial match`() {
        val expected = "CHEVAL"
        val guess = "ECLATE"

        val result =
            computeDiff(
                expectedWord = expected,
                guess = guess,
            )

        assertEquals(
            expected =
                listOf(
                    CharacterMatch.Partial('E'),
                    CharacterMatch.Partial('C'),
                    CharacterMatch.Partial('L'),
                    CharacterMatch.Partial('A'),
                    CharacterMatch.None('T'),
                    CharacterMatch.None('E'),
                ),
            actual = result,
        )
    }
}
