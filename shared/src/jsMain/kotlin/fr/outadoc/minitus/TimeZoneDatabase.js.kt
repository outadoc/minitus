package fr.outadoc.minitus

/**
 * Sur JS, kotlinx-datetime s'appuie sur js-joda, qui ne connaît que UTC et les décalages fixes
 * tant que `@js-joda/timezone` n'a pas été importé. Importer ce module enregistre la base de
 * données des fuseaux horaires auprès de `@js-joda/core`.
 */
@JsModule("@js-joda/timezone")
@JsNonModule
private external object JsJodaTimeZoneModule

internal actual fun loadTimeZoneDatabase() {
    // On référence le module pour que l'import ne soit pas supprimé par l'élimination de code mort.
    JsJodaTimeZoneModule
}
