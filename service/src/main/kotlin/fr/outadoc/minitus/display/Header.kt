package fr.outadoc.minitus.display

import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.VideotexBuilder

internal fun VideotexBuilder.displayHeader(puzzleNumber: Int?) {
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

    repeatChar('_', 40)

    if (puzzleNumber != null) {
        moveCursorTo(
            col = 37 - puzzleNumber.toString().length,
            line = 2
        )
        withCharacterSize(CharacterSize.DoubleHeight) {
            append("#%d".format(puzzleNumber))
        }
    }

    moveCursorTo(col = 1, line = 5)
}
