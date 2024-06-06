package org.alter.plugins.content.combat.specialattack

/**
 * @author Tom <rspsmods@gmail.com> || Cl0udS3c
 * @param energyRequired How much energy does the specialAttack cost.
 * @param executeOnSpecBar If it should be executed when the bar is clicked.
 */
data class SpecialAttack(val energyRequired: Int, val executeOnSpecBar: Boolean = false, val attack: CombatContext.() -> Unit)
