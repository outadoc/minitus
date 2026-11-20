package fr.outadoc.minitus.dictionary

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.until

private val EPOCH = LocalDate(2024, 12, 31)

internal fun LocalDate.getPuzzleNumber(): Int {
    if (this < EPOCH) {
        throw IllegalArgumentException(
            "La date d'aujourd'hui précède le $EPOCH et n'a pas de puzzle correspondant.",
        )
    }

    val daysSinceEpoch: Int = EPOCH.until(this, DateTimeUnit.DAY)
    return daysSinceEpoch
}
