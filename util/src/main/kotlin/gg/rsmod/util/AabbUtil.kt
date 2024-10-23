package gg.rsmod.util

/**
 * Utility methods for axis-aligned bounding boxes.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object AabbUtil {
    data class Box(val x: Int, val y: Int, val width: Int, val length: Int) {
        val x1: Int get() = x

        val x2: Int get() = x + width

        val y1: Int get() = y

        val y2: Int get() = y + length
    }

    /**
     * Checks to see if two AABB are bordering, but not overlapping.
     */
    fun areBordering(
        x1: Int,
        y1: Int,
        width1: Int,
        length1: Int,
        x2: Int,
        y2: Int,
        width2: Int,
        length2: Int,
    ): Boolean {
        val a = Box(x1, y1, width1 - 1, length1 - 1)
        val b = Box(x2, y2, width2 - 1, length2 - 1)

        if (b.x1 in a.x1..a.x2 && b.y1 in a.y1..a.y2 || b.x2 in a.x1..a.x2 && b.y2 in a.y1..a.y2) {
            return false
        }

        if (b.x1 > a.x2 + 1) {
            return false
        }

        if (b.x2 < a.x1 - 1) {
            return false
        }

        if (b.y1 > a.y2 + 1) {
            return false
        }

        if (b.y2 < a.y1 - 1) {
            return false
        }
        return true
    }

    fun areDiagonal(
        x1: Int,
        y1: Int,
        width1: Int,
        length1: Int,
        x2: Int,
        y2: Int,
        width2: Int,
        length2: Int,
    ): Boolean {
        val a = Box(x1, y1, width1 - 1, length1 - 1)
        val b = Box(x2, y2, width2 - 1, length2 - 1)

        /**
         * South-west diagonal tile.
         */
        if (a.x1 - 1 == b.x2 && a.y1 - 1 == b.y2) {
            return true
        }

        /**
         * South-east diagonal tile.
         */
        if (a.x2 + 1 == b.x2 && a.y1 - 1 == b.y2) {
            return true
        }

        /**
         * North-west diagonal tile.
         */
        if (a.x1 - 1 == b.x2 && a.y2 + 1 == b.y2) {
            return true
        }

        /**
         * North-east diagonal tile.
         */
        if (a.x2 + 1 == b.x2 && a.y2 + 1 == b.y2) {
            return true
        }

        return false
    }

    fun areOverlapping(
        x1: Int,
        y1: Int,
        width1: Int,
        length1: Int,
        x2: Int,
        y2: Int,
        width2: Int,
        length2: Int,
    ): Boolean {
        val a = Box(x1, y1, width1 - 1, length1 - 1)
        val b = Box(x2, y2, width2 - 1, length2 - 1)

        if (a.x1 > b.x2 || b.x1 > a.x2) {
            return false
        }

        if (a.y1 > b.y2 || b.y1 > a.y2) {
            return false
        }

        return true
    }
}
