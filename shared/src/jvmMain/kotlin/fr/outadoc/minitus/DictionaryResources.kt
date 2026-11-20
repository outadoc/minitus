package fr.outadoc.minitus

import fr.outadoc.minitus.dictionary.Dictionary
import java.io.File

private const val DICT_PATH = "/dict/fr/dict.txt"

private object Resources

/**
 * Charge le dictionnaire brut embarqué dans les ressources JVM.
 */
fun Dictionary.Companion.loadFromResources(): Dictionary {
    val stream =
        checkNotNull(Resources::class.java.getResourceAsStream(DICT_PATH)) {
            "Dictionnaire introuvable : $DICT_PATH"
        }

    return stream.bufferedReader(Charsets.UTF_8).useLines { lines ->
        fromRawWords(lines)
    }
}

/**
 * Génère la liste de mots prétraitée utilisée par le Worker Cloudflare.
 *
 * Usage : `GenerateWordListKt <fichier de sortie>`
 */
object GenerateWordList {
    @JvmStatic
    fun main(args: Array<String>) {
        val output = File(args.single())
        val dictionary = Dictionary.loadFromResources()

        output.parentFile?.mkdirs()
        output.writeText(dictionary.toWordList())

        println("${dictionary.size} mots écrits dans $output")
    }
}
