package fr.outadoc.minitus

import java.text.Normalizer

internal actual fun String.toNfd(): String = Normalizer.normalize(this, Normalizer.Form.NFD)
