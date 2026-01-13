package fr.outadoc.minitus

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minitus.dictionary.getPuzzleNumber
import fr.outadoc.minitus.dictionary.readWords
import fr.outadoc.minitus.screens.MinitusState
import fr.outadoc.minitus.screens.levelSelectionScreen
import fr.outadoc.minitus.screens.loseScreen
import fr.outadoc.minitus.screens.playingScreen
import fr.outadoc.minitus.screens.reduce
import fr.outadoc.minitus.screens.winScreen
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.minitus() {
    val dictionary: Set<String> = readWords(environment)

    routing {
        get("/status") {
            call.respond("OK")
        }
    }

    minitelService<MinitusState>(
        path = "/",
        version = "0.1",
        initialState = {
            MinitusState.Playing(
                guesses = emptyList(),
                puzzleNumber = today().getPuzzleNumber(),
            )
        },
    ) { request ->
        val nextState =
            request.state.reduce(
                userInput = request.userInput.firstOrNull().orEmpty(),
                event = request.event,
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

            is MinitusState.LevelSelection -> {
                levelSelectionScreen(
                    state = nextState,
                )
            }
        }
    }
}
