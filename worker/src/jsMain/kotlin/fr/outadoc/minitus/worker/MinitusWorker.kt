@file:OptIn(ExperimentalJsExport::class)

package fr.outadoc.minitus.worker

import fr.outadoc.minipavi.core.MinitelServiceHandler
import fr.outadoc.minipavi.core.cloudflare.ConsoleMinitelLogger
import fr.outadoc.minipavi.core.cloudflare.Headers
import fr.outadoc.minipavi.core.cloudflare.Request
import fr.outadoc.minipavi.core.cloudflare.Response
import fr.outadoc.minipavi.core.cloudflare.ResponseInit
import fr.outadoc.minipavi.core.cloudflare.toFetchHandler
import fr.outadoc.minitus.Minitus
import fr.outadoc.minitus.dictionary.Dictionary
import fr.outadoc.minitus.screens.MinitusState
import kotlinx.datetime.Clock
import kotlin.js.Promise

private var minitus: Minitus? = null

private val logger = ConsoleMinitelLogger()

private val handler =
    MinitelServiceHandler<MinitusState>(
        version = Minitus.VERSION,
        initialState = { requireMinitus().initialState() },
        logger = logger,
    ) { request ->
        requireMinitus().handle(request)
    }.toFetchHandler(path = "/", logger = logger)

/**
 * Charge la liste de mots générée par la tâche Gradle `generateWordList`.
 *
 * À appeler une seule fois, au chargement du module, depuis `worker/index.mjs`. Le mélange
 * qui détermine le mot du jour est calculé dans la foulée, pour que ce travail soit fait au
 * démarrage de l'isolate plutôt que pendant une requête.
 */
@JsExport
fun initDictionary(wordList: String) {
    val dictionary = Dictionary.fromWordList(wordList)
    dictionary.pickDailyWord(0)
    minitus = Minitus(dictionary = dictionary, clock = Clock.System)
}

private fun requireMinitus(): Minitus = checkNotNull(minitus) { "initDictionary() n'a pas été appelée" }

/**
 * Point d'entrée du Worker, réexporté par `worker/index.mjs`.
 *
 * Une fonction, et non une propriété : Kotlin/JS exporte les propriétés sous forme d'accesseurs.
 */
@JsExport
fun fetch(request: Request): Promise<Response> {
    if (request.method == "GET" && URL(request.url).pathname == "/status") {
        return Promise.resolve(textResponse("OK"))
    }

    return handler(request)
}

private external class URL(
    url: String,
) {
    val pathname: String
}

private fun textResponse(body: String): Response {
    val headers = Headers()
    headers.set("content-type", "text/plain; charset=utf-8")

    val init = js("{}").unsafeCast<ResponseInit>()
    init.status = 200
    init.headers = headers
    return Response(body = body, init = init)
}
