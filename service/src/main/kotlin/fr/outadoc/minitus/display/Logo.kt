package fr.outadoc.minitus.display

import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.VideotexBuilder

internal fun VideotexBuilder.displayLogo() {
    moveCursorTo(14, 2)

    withCharacterSize(CharacterSize.DoubleSize) {
        append('M')
        withInvertedBackground {
            withTextColor(Color.Yellow) {
                append('I')
            }
        }
        append("NI")
        withInvertedBackground {
            withTextColor(Color.Red) {
                append('T')
            }
        }
        append('U')
        withInvertedBackground {
            withTextColor(Color.Yellow) {
                append('S')
            }
        }
        appendLine()
    }

    withUnderline {
        repeatChar(' ', 39)
    }

    appendLine()
    appendLine()
}
