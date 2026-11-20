package fr.outadoc.minitus

import kotlinx.datetime.LocalDate

/**
 * Sur JS, kotlinx-datetime a besoin de `@js-joda/timezone` (et de sa base de fuseaux horaires)
 * pour gérer les fuseaux autres qu'UTC. On utilise plutôt l'API `Intl` native, disponible
 * dans Node.js comme dans Cloudflare Workers.
 */
private val formatter: dynamic =
    run {
        val options: dynamic = js("{}")
        options.timeZone = GameConstants.TIME_ZONE
        options.year = "numeric"
        options.month = "numeric"
        options.day = "numeric"
        js("new Intl.DateTimeFormat('en-US', options)")
    }

actual fun today(): LocalDate {
    val parts: Array<dynamic> = formatter.formatToParts(js("new Date()")).unsafeCast<Array<dynamic>>()

    fun part(type: String): Int =
        parts
            .first { part -> part.type == type }
            .value
            .unsafeCast<String>()
            .toInt()

    return LocalDate(
        year = part("year"),
        monthNumber = part("month"),
        dayOfMonth = part("day"),
    )
}
