package org.alter.game.model.region

import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.alter.game.model.Tile
import org.alter.game.model.World

/**
 * Stores and exposes [Chunk]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkSet(val world: World) {

    private val chunks = Object2ObjectOpenHashMap<ChunkCoords, Chunk>()

    internal val activeRegions = IntOpenHashSet()

    fun getActiveChunkCount(): Int = chunks.size

    fun getActiveRegionCount(): Int = activeRegions.size

    /**
     * Get the [Chunk] that corresponds to the given [chunks].
     *
     * @param tile
     * The [Tile] to get the [ChunkCoords] from.
     */
    fun getOrCreate(tile: Tile): Chunk = get(tile.chunkCoords, createIfNeeded = true)!!

    /**
     * Get the [Chunk] that corresponds to the given [chunks].
     *
     * @param tile
     * The [Tile] to get the [ChunkCoords] from.
     *
     * @param createIfNeeded
     * Create the [Chunk] if it does not already exist in our [chunks].
     */
    fun get(
        tile: Tile,
        createIfNeeded: Boolean = false,
    ): Chunk? = get(tile.chunkCoords, createIfNeeded)

    /**
     * Get the [Chunk] that corresponds to the given [chunks].
     *
     * @param coords
     * The [ChunkCoords] to get the [Chunk] for.
     *
     * @param createIfNeeded
     * Create the [Chunk] if it does not already exist in our [chunks].
     */
    fun get(
        coords: ChunkCoords,
        createIfNeeded: Boolean = false,
    ): Chunk? {
        val chunk = chunks[coords]
        if (chunk != null) {
            return chunk
        } else if (!createIfNeeded) {
            return null
        }
        val regionId = coords.toTile().regionId
        val newChunk = Chunk(coords)
        newChunk.createEntityContainers()

        chunks[coords] = newChunk
        if (activeRegions.add(regionId)) {
            world.definitions.createRegion(world, regionId)
        }
        return newChunk
    }

    fun remove(coords: ChunkCoords): Boolean = chunks.remove(coords) != null
}
