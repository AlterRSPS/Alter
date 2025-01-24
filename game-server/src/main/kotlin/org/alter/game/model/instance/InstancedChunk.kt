package org.alter.game.model.instance

/**
 * An 8x8 tile area (aka, chunk) inside an instanced area.
 *
 * @param packed
 * A bit-packed value of the rotated, original [org.alter.game.model.region.ChunkCoords]
 * (to copy).
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class InstancedChunk(var packed: Int) {
    val rot: Int get() = (packed shr 1) and 0x3

    // TODO ADVO check over these when sober
    val height: Int get() = (packed shr 24) and 0x3
    val x: Int get() = zoneX shl 3
    val y: Int get() = zoneZ shl 3
    val zoneX: Int get() = (packed shr 14 and 0x3FF)
    val zoneZ: Int get() = (packed shr 3 and 0x7FF)
}
