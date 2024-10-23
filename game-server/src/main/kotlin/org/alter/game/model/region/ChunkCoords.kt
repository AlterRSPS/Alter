package org.alter.game.model.region

import gg.rsmod.util.toStringHelper
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.alter.game.model.Tile

/**
 * Represents coordinates that can be occupied by a [Chunk].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkCoords(val x: Int, val y: Int) {
    fun toTile(): Tile = Tile((x + 6) shl 3, (y + 6) shl 3)

    fun getSurroundingCoords(chunkRadius: Int = Chunk.CHUNK_VIEW_RADIUS): ObjectOpenHashSet<ChunkCoords> {
        val surrounding = ObjectOpenHashSet<ChunkCoords>()

        for (x in -chunkRadius..chunkRadius) {
            for (z in -chunkRadius..chunkRadius) {
                surrounding.add(ChunkCoords(this.x + x, this.y + y))
            }
        }
        return surrounding
    }

    override fun toString(): String = toStringHelper().add("x", x).add("y", y).toString()

    override fun equals(other: Any?): Boolean {
        if (other is ChunkCoords) {
            return other.x == x && other.y == y
        }
        return false
    }

    override fun hashCode(): Int = (x shl 16) or y

    companion object {
        fun fromTile(
            x: Int,
            y: Int,
        ): ChunkCoords = ChunkCoords(x, y)

        fun fromTile(tile: Tile): ChunkCoords = fromTile(tile.topLeftRegionX, tile.topLeftRegionY)
    }
}
