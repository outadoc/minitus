package fr.outadoc.minitus

import kotlinx.datetime.LocalDate

/**
 * Renvoie la date du jour dans le fuseau horaire du jeu.
 */
expect fun today(): LocalDate
