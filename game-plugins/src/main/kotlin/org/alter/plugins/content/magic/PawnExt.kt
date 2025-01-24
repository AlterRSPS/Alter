package org.alter.plugins.content.magic

import dev.openrune.cache.CacheManager.getAnim
import org.alter.api.ext.getWildernessLevel
import org.alter.api.ext.message
import org.alter.game.model.LockState
import org.alter.game.model.Tile
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.move.moveTo
import org.alter.game.model.queue.TaskPriority

fun Player.canTeleport(type: TeleportType): Boolean {
    val currWildLvl = tile.getWildernessLevel()
    val wildLvlRestriction = type.wildLvlRestriction

    if (!lock.canTeleport()) {
        return false
    }

    if (currWildLvl > wildLvlRestriction) {
        message("A mysterious force blocks your teleport spell!")
        message("You can't use this teleport after level $wildLvlRestriction wilderness.")
        return false
    }

    return true
}

fun Pawn.prepareForTeleport() {
    resetInteractions()
    clearHits()
}

fun Pawn.teleport(
    endTile: Tile,
    type: TeleportType,
) {
    lock = LockState.FULL_WITH_DAMAGE_IMMUNITY

    queue(TaskPriority.STRONG) {
        prepareForTeleport()

        animate(type.animation)
        type.graphic?.let {
            graphic(it)
        }

        wait(type.teleportDelay)

        moveTo(endTile)

        type.endAnimation?.let {
            animate(it)
        }

        type.endGraphic?.let {
            graphic(it)
        }

        type.endAnimation?.let {
            val def = getAnim(it)
            wait(def.cycleLength)
        }

        animate(-1)
        unlock()
    }
}
