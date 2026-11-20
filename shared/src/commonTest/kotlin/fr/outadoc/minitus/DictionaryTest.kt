package fr.outadoc.minitus

import fr.outadoc.minitus.dictionary.Dictionary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DictionaryTest {
    @Test
    fun `Raw words are normalized, filtered and deduplicated`() {
        val dictionary =
            Dictionary.fromRawWords(
                sequenceOf("à", "abaissa", "Abaissa", "élève", "aujourd'hui", "anticonstitutionnellement", "ŒUVRE", "zythum"),
            )

        assertEquals(3, dictionary.size)
        assertTrue("ABAISSA" in dictionary)
        assertTrue("ELEVE" in dictionary)
        assertTrue("ZYTHUM" in dictionary)
        assertFalse("A" in dictionary)
        assertFalse("ANTICONSTITUTIONNELLEMENT" in dictionary)
    }

    @Test
    fun `A word list round-trips and keeps the same daily words`() {
        val dictionary = Dictionary.of(listOf("POMME", "LAPIN", "CHEVAL", "ROUGE", "TABLE"))
        val parsed = Dictionary.fromWordList(dictionary.toWordList())

        assertEquals(dictionary.size, parsed.size)
        (0..20).forEach { puzzleNumber ->
            assertEquals(dictionary.pickDailyWord(puzzleNumber), parsed.pickDailyWord(puzzleNumber))
        }
    }

    @Test
    fun `An unsorted word list is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            Dictionary.fromWordList("POMME\nLAPIN\n")
        }
    }

    @Test
    fun `The daily word is the same on every platform`() {
        // Ces valeurs ont été calculées sur la JVM ; elles doivent être identiques sur JS.
        val dictionary = Dictionary.of(('A'..'Z').map { c -> "MOT$c$c" })

        assertEquals(
            expected = EXPECTED_DAILY_WORDS,
            actual = (0 until EXPECTED_DAILY_WORDS.size).map { dictionary.pickDailyWord(it) },
        )
    }

    private companion object {
        val EXPECTED_DAILY_WORDS =
            listOf("MOTMM", "MOTGG", "MOTEE", "MOTQQ", "MOTOO", "MOTNN", "MOTFF", "MOTAA", "MOTDD", "MOTJJ")
    }
}
