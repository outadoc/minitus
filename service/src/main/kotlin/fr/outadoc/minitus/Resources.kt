package fr.outadoc.minitus

import kotlinx.io.Source
import kotlinx.io.asSource
import kotlinx.io.buffered

private object Resources

fun readResource(path: String): Source {
    return Resources::class.java.getResourceAsStream(path)!!
        .asSource()
        .buffered()
}
