package org.alter.game.model.collision

import org.alter.game.model.Tile
import org.alter.game.model.region.Chunk
import org.rsmod.routefinder.LineValidator
import org.rsmod.routefinder.collision.CollisionFlagMap
import org.rsmod.routefinder.loc.LocShapeConstants

fun CollisionFlagMap.isClipped(tile: Tile): Boolean = get(tile) != 0
const val WALL_DIAGONAL = LocShapeConstants.WALL_DIAGONAL;
const val BLOCKED_TILE = 0x1
const val BRIDGE_TILE = 0x2
const val ROOF_TILE = 0x4

/**
 * Casts a line using Bresenham's Line Algorithm with point A [start] and
 * point B [target] being its two points and makes sure that there's no
 * collision flag that can block movement from and to both points. This function
 * was originally CollisionManager#raycast in rsmod1.
 *
 * @param projectile
 * Projectiles have a higher tolerance for certain objects when the object's
 * metadata explicitly allows them to.
 */
fun LineValidator.rayCast(
    start: Tile,
    target: Tile,
    projectile: Boolean,
): Boolean {
    check(start.height == target.height) { "Tiles must be on the same height level." }
    return if (projectile) {
        hasLineOfSight(start.height, start.x, start.z, target.x, target.z)
    } else {
        hasLineOfWalk(start.height, start.x, start.z, target.x, target.z)
    }
}

/**
 * Extension stub function that will be used for setting a specific coordinate/height
 * to either impenetrable or not.
 */
fun CollisionFlagMap.block(newChunk: Chunk, chunkH: Int, lx: Int, lz: Int, impenetrable: Boolean) {
    // TODO impl this function.
}