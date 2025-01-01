package fr.outadoc.minitus.screens

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MinitusState {
    val date: LocalDate
    val guesses: List<String>

    @Serializable
    @SerialName("intro")
    data class Playing(
        override val date: LocalDate,
        override val guesses: List<String>,
        val lastInputError: Error? = null,
    ) : MinitusState

    @Serializable
    @SerialName("win")
    data class Win(
        override val date: LocalDate,
        override val guesses: List<String>,
    ) : MinitusState

    @Serializable
    @SerialName("lose")
    data class Lose(
        override val date: LocalDate,
        override val guesses: List<String>,
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
    date: LocalDate,
    userInput: String,
    dictionary: Set<String>,
): MinitusState {
    return when (this) {
        is MinitusState.Playing -> reduce(date, userInput, dictionary)
        is MinitusState.Win -> this
        is MinitusState.Lose -> this
    }
}
