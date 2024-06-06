package org.alter.api.ext

import org.alter.game.model.combat.PawnHit

fun PawnHit.landed(): Boolean = landed

fun PawnHit.blocked(): Boolean = !landed

fun PawnHit.getClientHitDelay(): Int = hit.damageDelay * 50
