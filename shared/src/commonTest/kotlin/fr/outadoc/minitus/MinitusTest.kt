package fr.outadoc.minitus

import fr.outadoc.minitus.dictionary.Dictionary
import fr.outadoc.minitus.screens.MinitusState
import kotlin.test.Test
import kotlin.test.assertEquals

class MinitusTest {
    private val dictionary = Dictionary.of(listOf("LAPIN", "POMME", "TABLE"))

    @Test
    fun `The initial state is today's puzzle, in Paris time`() {
        val minitus =
            Minitus(
                dictionary = dictionary,
                // 18 juillet 2025 à 0h30, heure de Paris
                clock = FixedClock("2025-07-17T22:30:00Z"),
            )

        assertEquals(
            expected = MinitusState.Playing(puzzleNumber = 199, guesses = emptyList()),
            actual = minitus.initialState(),
        )
    }
}
