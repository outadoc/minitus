package fr.outadoc.minitus

internal actual fun String.toNfd(): String = asDynamic().normalize("NFD").unsafeCast<String>()
