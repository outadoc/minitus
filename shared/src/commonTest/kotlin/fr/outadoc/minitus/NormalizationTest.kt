package fr.outadoc.minitus

import kotlin.test.Test
import kotlin.test.assertEquals

class NormalizationTest {
    @Test
    fun `Normalize word with only uppercase letters`() {
        val word = "HELLO"
        val normalized = word.normalize()
        assertEquals("HELLO", normalized)
    }

    @Test
    fun `Normalize word with mixed lower and upper case letters`() {
        val word = "HeLlO"
        val normalized = word.normalize()
        assertEquals("HELLO", normalized)
    }

    @Test
    fun `Normalize word with lowercase letters and accents`() {
        val word = "éàè"
        val normalized = word.normalize()
        assertEquals("EAE", normalized)
    }

    @Test
    fun `Normalize word with uppercase letters and accents`() {
        val word = "ÉÀÈ"
        val normalized = word.normalize()
        assertEquals("EAE", normalized)
    }
}
