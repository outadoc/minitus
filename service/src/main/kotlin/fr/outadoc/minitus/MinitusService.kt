package fr.outadoc.minitus

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minitus.dictionary.Dictionary
import fr.outadoc.minitus.screens.MinitusState
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.minitus() {
    val dictionary: Dictionary = Dictionary.loadFromResources()
    environment.log.info("Chargé ${dictionary.size} mots avec succès")

    routing {
        get("/status") {
            call.respond("OK")
        }
    }

    minitelService<MinitusState>(
        path = "/",
        version = Minitus.VERSION,
        initialState = Minitus::initialState,
    ) { request ->
        Minitus.handle(
            request = request,
            dictionary = dictionary,
        )
    }
}
