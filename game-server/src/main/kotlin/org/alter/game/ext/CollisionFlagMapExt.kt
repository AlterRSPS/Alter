package org.alter.game.ext

import org.alter.game.model.Direction
import org.alter.game.model.collision.CollisionFlag.Companion.pawnFlags
import org.alter.game.model.collision.CollisionFlag.Companion.projectileFlags
import org.alter.game.model.collision.CollisionUpdate
import org.rsmod.game.pathfinder.collision.CollisionFlagMap
import kotlin.experimental.or

fun CollisionFlagMap.applyUpdate(update: CollisionUpdate) {
    val map = update.flags

    for (entry in map.entries) {
        val tile = entry.key

        val pawns = pawnFlags()
        val projectiles = projectileFlags()

        for (flag in entry.value) {
            val direction = flag.direction
            if (direction == Direction.NONE) {
                continue
            }

            val orientation = direction.orientationValue
            add(
                absoluteX = tile.x,
                absoluteZ = tile.z,
                level = tile.height,
                mask = if (flag.impenetrable) {
                    projectiles[orientation].getBitAsShort() or pawns[orientation].getBitAsShort()
                } else {
                    pawns[orientation].getBitAsShort()
                }.toInt(),
            )
        }
    }
}