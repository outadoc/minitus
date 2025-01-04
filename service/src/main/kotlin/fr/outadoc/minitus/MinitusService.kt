package fr.outadoc.minitus

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minitus.dictionary.getPuzzleNumber
import fr.outadoc.minitus.dictionary.readWords
import fr.outadoc.minitus.screens.MinitusState
import fr.outadoc.minitus.screens.loseScreen
import fr.outadoc.minitus.screens.playingScreen
import fr.outadoc.minitus.screens.reduce
import fr.outadoc.minitus.screens.winScreen
import io.ktor.server.application.Application
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Application.minitus() {
    val clock = Clock.System
    val tz = TimeZone.of("Europe/Paris")

    val dictionary: Set<String> = readWords(environment)

    minitelService<MinitusState>(
        path = "/",
        version = "0.1",
        initialState = {
            MinitusState.Playing(
                guesses = emptyList(),
                puzzleNumber = clock.today(at = tz).getPuzzleNumber(),
            )
        },
    ) { request ->
        val nextState =
            request.state.reduce(
                userInput = request.userInput.firstOrNull().orEmpty(),
                dictionary = dictionary,
            )

        when (nextState) {
            is MinitusState.Playing -> {
                playingScreen(
                    state = nextState,
                    dictionary = dictionary,
                )
            }

            is MinitusState.Lose -> {
                loseScreen(
                    state = nextState,
                    dictionary = dictionary,
                )
            }

            is MinitusState.Win -> {
                winScreen(
                    state = nextState,
                    dictionary = dictionary,
                )
            }
        }
    }
}

private fun Clock.today(at: TimeZone): LocalDate {
    return now().toLocalDateTime(at).date
}
