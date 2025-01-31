package org.alter.plugins.content.mechanics.poison

import org.alter.game.model.attr.POISON_TICKS_LEFT_ATTR
import org.alter.game.model.timer.POISON_TIMER

val POISON_TICK_DELAY = 25

onPlayerDeath {
    player.timers.remove(POISON_TIMER)
    Poison.setHpOrb(player, Poison.OrbState.NONE)
}

onTimer(POISON_TIMER) {
    val pawn = pawn
    val ticksLeft = pawn.attr[POISON_TICKS_LEFT_ATTR] ?: 0

    if (ticksLeft == 0) {
        if (pawn is Player) {
            Poison.setHpOrb(pawn, Poison.OrbState.NONE)
        }
        return@onTimer
    }

    if (ticksLeft > 0) {
        pawn.attr[POISON_TICKS_LEFT_ATTR] = ticksLeft - 1
        pawn.hit(damage = Poison.getDamageForTicks(ticksLeft), type = HitType.POISON)
    } else if (ticksLeft < 0) {
        pawn.attr[POISON_TICKS_LEFT_ATTR] = ticksLeft + 1
    }

    pawn.timers[POISON_TIMER] = POISON_TICK_DELAY
}
