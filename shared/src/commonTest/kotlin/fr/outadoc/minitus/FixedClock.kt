package fr.outadoc.minitus

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class FixedClock(
    private val instant: Instant,
) : Clock {
    constructor(isoInstant: String) : this(Instant.parse(isoInstant))

    override fun now(): Instant = instant
}
