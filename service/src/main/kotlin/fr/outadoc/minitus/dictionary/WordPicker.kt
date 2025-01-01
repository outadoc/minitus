package fr.outadoc.minitus.dictionary

import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlin.random.Random

private const val SEED = 1370784130107967936L
private val EPOCH = LocalDate(2024, 12, 31)

internal fun Set<String>.pickDailyWord(today: LocalDate): String {
    check(isNotEmpty()) { "La liste de mots est vide" }
    check(today >= EPOCH) { "La date d'aujourd'hui précède le $EPOCH" }

    // On mélange la liste en utilisant un seed fixe
    val random = Random(SEED)
    val shuffledWords: List<String> = sorted().shuffled(random)

    // Chaque mot de la liste correspond à un jour, dans l'ordre, depuis l'epoch défini
    val daysSinceEpoch: Int = (today - EPOCH).days
    return shuffledWords[daysSinceEpoch % shuffledWords.size]
}
