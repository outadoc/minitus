package fr.outadoc.minitus.display

import fr.outadoc.minipavi.videotex.VideotexBuilder

fun VideotexBuilder.displayFooter() {
    moveCursorTo(1, 23)
    repeatChar('_', 40)
    append("Jouer à un niveau précédent :  ")
    withInvertedBackground {
        appendLine("SOMMAIRE")
    }
}
