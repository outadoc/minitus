package fr.outadoc.minitus.dictionary

import fr.outadoc.minitus.GameConstants
import fr.outadoc.minitus.normalize
import kotlin.random.Random

private const val SEED = 1370784130107967936L

/**
 * La liste des mots acceptés par le jeu, triée par ordre alphabétique.
 *
 * Le mélange qui détermine le mot de chaque jour est calculé une seule fois, à la première
 * utilisation, puis réutilisé pour toutes les requêtes suivantes.
 */
class Dictionary private constructor(
    private val sortedWords: List<String>,
) {
    val size: Int
        get() = sortedWords.size

    // On mélange la liste en utilisant un seed fixe.
    // Random(seed) produit la même séquence sur toutes les plateformes.
    private val shuffledWords: List<String> by lazy {
        sortedWords.shuffled(Random(SEED))
    }

    operator fun contains(word: String): Boolean = sortedWords.binarySearch(word) >= 0

    fun pickDailyWord(puzzleNumber: Int): String {
        check(sortedWords.isNotEmpty()) { "La liste de mots est vide" }

        // Chaque mot de la liste correspond à un jour, dans l'ordre, depuis l'epoch défini
        return shuffledWords[puzzleNumber % shuffledWords.size]
    }

    /**
     * Sérialise le dictionnaire dans le format lu par [fromWordList] : un mot par ligne.
     */
    fun toWordList(): String = sortedWords.joinToString(separator = "\n", postfix = "\n")

    companion object {
        /**
         * Crée un dictionnaire à partir de mots déjà validés.
         */
        fun of(words: Collection<String>): Dictionary = Dictionary(words.distinct().sorted())

        /**
         * Crée un dictionnaire à partir de la liste brute (une entrée par ligne, avec accents et
         * minuscules), en ne gardant que les mots jouables.
         */
        fun fromRawWords(lines: Sequence<String>): Dictionary =
            of(
                lines
                    .map { line -> line.normalize() }
                    .filter { word -> word.matches(GameConstants.ALLOWED_WORD_REGEX) }
                    .toSet(),
            )

        /**
         * Lit un dictionnaire produit par [toWordList].
         *
         * Ce format évite de normaliser et de trier les mots à l'exécution, ce qui compte sur
         * une plateforme au temps CPU limité comme Cloudflare Workers.
         */
        fun fromWordList(text: String): Dictionary {
            val words = text.split('\n').filter { word -> word.isNotEmpty() }

            for (i in 1 until words.size) {
                require(words[i - 1] < words[i]) {
                    "La liste de mots n'est pas triée : ${words[i - 1]} précède ${words[i]}"
                }
            }

            return Dictionary(words)
        }
    }
}
