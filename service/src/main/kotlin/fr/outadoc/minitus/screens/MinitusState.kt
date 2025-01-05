package fr.outadoc.minitus.screens

import fr.outadoc.minipavi.core.model.FunctionKey
import fr.outadoc.minipavi.core.model.GatewayRequest
import kotlinx.serialization.Serializable

@Serializable
sealed interface MinitusState {
    @Serializable
    data class Playing(
        val puzzleNumber: Int,
        val guesses: List<String>,
        val lastInputError: Error? = null,
    ) : MinitusState {
        @Serializable
        sealed interface Error {
            @Serializable
            data object NotInDictionary : Error

            @Serializable
            data object InvalidLength : Error

            @Serializable
            data object InvalidFirstLetter : Error
        }
    }

    @Serializable
    data class Win(
        val puzzleNumber: Int,
        val guesses: List<String>,
    ) : MinitusState

    @Serializable
    data class Lose(
        val puzzleNumber: Int,
        val guesses: List<String>,
    ) : MinitusState

    @Serializable
    data class LevelSelection(
        val error: Error? = null,
    ) : MinitusState {
        @Serializable
        sealed interface Error {
            @Serializable
            data object InvalidLevelRange : Error

            @Serializable
            data object InvalidLevelFormat : Error
        }
    }
}

internal fun MinitusState.reduce(
    userInput: String,
    event: GatewayRequest.Event,
    dictionary: Set<String>,
): MinitusState {
    if (event is GatewayRequest.Event.KeyboardInput && event.key == FunctionKey.Sommaire) {
        return MinitusState.LevelSelection()
    }

    return when (this) {
        is MinitusState.Playing -> {
            reduce(
                puzzleNumber = puzzleNumber,
                userInput = userInput,
                dictionary = dictionary,
            )
        }

        is MinitusState.LevelSelection -> {
            reduce(
                userInput = userInput,
            )
        }

        is MinitusState.Win -> {
            this
        }

        is MinitusState.Lose -> {
            this
        }
    }
}
