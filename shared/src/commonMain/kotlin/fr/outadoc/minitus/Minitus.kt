package fr.outadoc.minitus

import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minitus.dictionary.Dictionary
import fr.outadoc.minitus.dictionary.getPuzzleNumber
import fr.outadoc.minitus.screens.MinitusState
import fr.outadoc.minitus.screens.levelSelectionScreen
import fr.outadoc.minitus.screens.loseScreen
import fr.outadoc.minitus.screens.playingScreen
import fr.outadoc.minitus.screens.reduce
import fr.outadoc.minitus.screens.winScreen

/**
 * Le service Minitus, indépendant de la plateforme d'hébergement.
 */
object Minitus {
    const val VERSION = "0.1"

    fun initialState(): MinitusState =
        MinitusState.Playing(
            guesses = emptyList(),
            puzzleNumber = today().getPuzzleNumber(),
        )

    fun handle(
        request: GatewayRequest<MinitusState>,
        dictionary: Dictionary,
    ): ServiceResponse<MinitusState> {
        val nextState =
            request.state.reduce(
                userInput = request.userInput.firstOrNull().orEmpty(),
                event = request.event,
                dictionary = dictionary,
            )

        return when (nextState) {
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
