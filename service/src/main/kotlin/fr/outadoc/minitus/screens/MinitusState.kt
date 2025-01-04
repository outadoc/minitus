package fr.outadoc.minitus.screens

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MinitusState {
    @Serializable
    @SerialName("intro")
    data class Playing(
        val puzzleNumber: Int,
        val guesses: List<String>,
        val lastInputError: Error? = null,
    ) : MinitusState

    @Serializable
    @SerialName("win")
    data class Win(
        val puzzleNumber: Int,
        val guesses: List<String>,
    ) : MinitusState

    @Serializable
    @SerialName("lose")
    data class Lose(
        val puzzleNumber: Int,
        val guesses: List<String>,
    ) : MinitusState

    @Serializable
    sealed interface Error {
        @Serializable
        data object NotInDictionary : Error

        @Serializable
        data object InvalidLength : Error
    }
}

internal fun MinitusState.reduce(
    userInput: String,
    dictionary: Set<String>,
): MinitusState {
    return when (this) {
        is MinitusState.Playing -> {
            reduce(puzzleNumber, userInput, dictionary)
        }

        is MinitusState.Win -> this
        is MinitusState.Lose -> this
    }
}
