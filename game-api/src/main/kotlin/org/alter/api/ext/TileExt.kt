package org.alter.api.ext

import org.alter.game.model.Tile
import org.alter.game.model.World

fun Tile.isMulti(world: World): Boolean {
    val region = regionId
    val chunk = chunkCoords.hashCode()
    return world.getMultiCombatChunks().contains(chunk) || world.getMultiCombatRegions().contains(region)
}

fun Tile.getWildernessLevel(): Int {
    if (x !in 2941..3392 || z !in 3524..3968) {
        return 0
    }

    val y = if (this.z > 6400) this.z - 6400 else this.z
    return (((y - 3525) shr 3) + 1)
}
