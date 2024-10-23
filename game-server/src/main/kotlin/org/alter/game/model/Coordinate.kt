package org.alter.game.model

/**
 * A coordinate is similar to a [Tile], however it stores each vector value
 * separately.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Coordinate(val x: Int, val y: Int, val height: Int) {
    /**
     * Returns the local tile of our region relative to the current [x] and [z].
     *
     * The [other] tile will always have coords equal to or greater than our own.
     */
    fun toLocal(other: Tile): Tile = Tile(((other.x shr 3) - (x shr 3)) shl 3, ((other.y shr 3) - (y shr 3)) shl 3, height)

    val tile: Tile get() = Tile(x, y, height)
}
