package org.alter.game.model

import org.rsmod.routefinder.flag.CollisionFlag

/**
 * Represents cardinal and ordinal directions in the game.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class Direction(
    val orientationValue: Int,
    val walkValue: Int,
    val faceNpc: Int,
) {
    NORTH_WEST(orientationValue = 0, walkValue = 5, faceNpc = 4),

    NORTH(orientationValue = 1, walkValue = 6, faceNpc = 5),

    NORTH_EAST(orientationValue = 2, walkValue = 7, faceNpc = 6),

    EAST(orientationValue = 4, walkValue = 4, faceNpc = 7),

    SOUTH_EAST(orientationValue = 7, walkValue = 2, faceNpc = 0),

    SOUTH(orientationValue = 6, walkValue = 1, faceNpc = 1),

    SOUTH_WEST(orientationValue = 5, walkValue = 0, faceNpc = 2),

    WEST(orientationValue = 3, walkValue = 3, faceNpc = 3),

    NONE(orientationValue = -1, walkValue = -1, faceNpc = -1), ;

    fun isDiagonal(): Boolean = this == SOUTH_EAST || this == SOUTH_WEST || this == NORTH_EAST || this == NORTH_WEST

    fun getDeltaX(): Int =
        when (this) {
            SOUTH_EAST, NORTH_EAST, EAST -> 1
            SOUTH_WEST, NORTH_WEST, WEST -> -1
            else -> 0
        }

    fun getDeltaZ(): Int =
        when (this) {
            NORTH_WEST, NORTH_EAST, NORTH -> 1
            SOUTH_WEST, SOUTH_EAST, SOUTH -> -1
            else -> 0
        }

    fun getOpposite(): Direction =
        when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
            NORTH_WEST -> SOUTH_EAST
            NORTH_EAST -> SOUTH_WEST
            SOUTH_EAST -> NORTH_WEST
            SOUTH_WEST -> NORTH_EAST
            else -> NONE
        }

    fun getDiagonalComponents(): Array<Direction> =
        when (this) {
            NORTH_EAST -> arrayOf(NORTH, EAST)
            NORTH_WEST -> arrayOf(NORTH, WEST)
            SOUTH_EAST -> arrayOf(SOUTH, EAST)
            SOUTH_WEST -> arrayOf(SOUTH, WEST)
            else -> throw IllegalArgumentException("Must provide a diagonal direction.")
        }

    val angle: Int
        get() {
            return when (this) {
                SOUTH -> 0
                SOUTH_WEST -> 256
                WEST -> 512
                NORTH_WEST -> 768
                NORTH -> 1024
                NORTH_EAST -> 1280
                EAST -> 1536
                SOUTH_EAST -> 1792
                NONE -> throw IllegalStateException("Invalid direction: $this")
            }
        }

    companion object {
        val NESW = arrayOf(NORTH, EAST, SOUTH, WEST)

        val WNES = arrayOf(WEST, NORTH, EAST, SOUTH)

        val WNES_DIAGONAL = arrayOf(NORTH_WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST)

        val RS_ORDER = arrayOf(WEST, EAST, NORTH, SOUTH, SOUTH_WEST, SOUTH_EAST, NORTH_WEST, NORTH_EAST)

        val ANGLED_ORDER = arrayOf(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST)

        fun getForAngle(angle: Int): Direction = ANGLED_ORDER[angle / 45]

        fun between(
            current: Tile,
            next: Tile,
        ): Direction {
            val deltaX = next.x - current.x
            val deltaZ = next.z - current.z

            return fromDeltas(deltaX, deltaZ)
        }

        private fun fromDeltas(
            deltaX: Int,
            deltaZ: Int,
        ): Direction {
            if (deltaX < 0) {
                if (deltaZ < 0) {
                    return SOUTH_WEST
                } else if (deltaZ > 0) {
                    return NORTH_WEST
                }
                return WEST
            } else if (deltaX > 0) {
                if (deltaZ < 0) {
                    return SOUTH_EAST
                } else if (deltaZ > 0) {
                    return NORTH_EAST
                }
                return EAST
            }
            return if (deltaZ < 0) SOUTH else NORTH
        }

        fun getDirectionFlag(direction: Direction): Int {
            return when (direction) {
                NORTH -> CollisionFlag.WALL_SOUTH
                EAST -> CollisionFlag.WALL_WEST
                SOUTH -> CollisionFlag.WALL_NORTH
                WEST -> CollisionFlag.WALL_EAST
                NORTH_EAST -> CollisionFlag.WALL_SOUTH_WEST
                NORTH_WEST -> CollisionFlag.WALL_SOUTH_EAST
                SOUTH_EAST -> CollisionFlag.WALL_NORTH_WEST
                SOUTH_WEST -> CollisionFlag.WALL_NORTH_EAST
                else -> 0
            }
        }

        fun calculateAttackDirection(
            npc: Tile,
            player: Tile,
        ): Direction {
            // Calculate the difference in X and Z coordinates between the NPC and the player
            val deltaX = player.x - npc.x
            val deltaZ = player.z - npc.z

            // Return the direction from the NPC to the player
            return fromDeltas(deltaX, deltaZ)
        }
    }
}
