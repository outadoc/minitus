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
import kotlinx.datetime.Clock

/**
 * Le service Minitus, indépendant de la plateforme d'hébergement.
 *
 * @param dictionary Les mots acceptés par le jeu.
 * @param clock L'horloge qui détermine le puzzle du jour.
 */
class Minitus(
    private val dictionary: Dictionary,
    private val clock: Clock = Clock.System,
) {
    private val latestPuzzle: Int
        get() = clock.today().getPuzzleNumber()

    fun initialState(): MinitusState =
        MinitusState.Playing(
            guesses = emptyList(),
            puzzleNumber = latestPuzzle,
        )

    fun handle(request: GatewayRequest<MinitusState>): ServiceResponse<MinitusState> {
        val latestPuzzle = latestPuzzle
        val nextState =
            request.state.reduce(
                userInput = request.userInput.firstOrNull().orEmpty(),
                event = request.event,
                dictionary = dictionary,
                latestPuzzle = latestPuzzle,
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
                    latestPuzzle = latestPuzzle,
                )
            }
        }
    }

    companion object {
        const val VERSION = "0.1"
    }
}
