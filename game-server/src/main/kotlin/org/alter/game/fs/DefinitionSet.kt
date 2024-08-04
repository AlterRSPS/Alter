package org.alter.game.fs

import dev.openrune.cache.*
import dev.openrune.cache.filestore.loadLocations
import dev.openrune.cache.filestore.loadTerrain
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.model.Direction
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.collision.CollisionUpdate
import org.alter.game.model.entity.StaticObject
import org.alter.game.model.region.ChunkSet
import org.alter.game.service.xtea.XteaKeyService
import org.rsmod.game.pathfinder.flag.CollisionFlag
import java.io.IOException

/**
 * A [DefinitionSet] is responsible for loading any relevant metadata found in
 * the game resources.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DefinitionSet {
    private var xteaService: XteaKeyService? = null

    fun loadRegions(
        world: World,
        chunks: ChunkSet,
        regions: IntArray,
    ) {
        val start = System.currentTimeMillis()

        var loaded = 0
        regions.forEach { region ->
            if (chunks.activeRegions.add(region)) {
                if (createRegion(world, region)) {
                    loaded++
                }
            }
        }
        logger.info { "Loaded $loaded regions in ${System.currentTimeMillis() - start}ms" }
    }

    /**
     * Creates an 8x8 [gg.rsmod.game.model.region.Chunk] region.
     */
    fun createRegion(
        world: World,
        id: Int,
    ): Boolean {
        if (xteaService == null) {
            xteaService = world.getService(XteaKeyService::class.java)
        }

        val x = id shr 8
        val z = id and 0xFF

        val mapData = CacheManager.cache.data(MAPS, "m${x}_$z") ?: return false

        val baseX: Int = id shr 8 and 255 shl 6
        val baseY: Int = id and 255 shl 6

        val blocked = hashSetOf<Tile>()
        val bridges = hashSetOf<Tile>()

        val tiles = loadTerrain(mapData)

        for (height in 0 until 4) {
            for (lx in 0 until 64) {
                for (lz in 0 until 64) {
                    val bridge = tiles[1][lx][lz].settings.toInt() and 0x2 != 0
                    if (bridge) {
                        bridges.add(Tile(baseX + lx, baseY + lz, height))
                    }
                    val blockedTile = tiles[height][lx][lz].settings.toInt() and 0x1 != 0
                    if (blockedTile) {
                        val level = if (bridge) (height - 1) else height
                        if (level < 0) continue
                        blocked.add(Tile(baseX + lx, baseY + lz, level))
                    }
                }
            }
        }

        /*
         * Apply the blocked tiles to the collision detection.
         */
        val blockedTileBuilder = CollisionUpdate.Builder()
        blockedTileBuilder.setType(CollisionUpdate.Type.ADD)
        blocked.forEach { tile ->
            world.chunks.getOrCreate(tile).blockedTiles.add(tile)
            blockedTileBuilder.putTile(tile, false, *Direction.NESW)
            world.collisionFlags.add(
                absoluteX = tile.x,
                absoluteZ = tile.z,
                level = tile.height,
                // Not impenetrable, so not adding projectile block flags
                // No need to add direction blocking either, as this is just a tile that's blocked.
                mask = CollisionFlag.FLOOR or CollisionFlag.FLOOR_DECORATION,
            )
        }
        world.collision.applyUpdate(blockedTileBuilder.build())
        /**
         * EDIT: turns out i was wrong. the assumption made here didn't pan out as expected. the bandos godwars room door ended up having different flags to before.
         *
         * instead, i just use
         * world.collisionFlags.applyUpdate(update)
         *
         * like in the other places, and that works
         *
         */
        if (xteaService == null) {
            /*
             * If we don't have an [XteaKeyService], then we assume we don't
             * need to decrypt the files through xteas. This means the objects
             * from each region has to be decrypted a different way.
             *
             * If this is the case, you need to use [gg.rsmod.game.model.region.Chunk.addEntity]
             * to add the object to the world for collision detection.
             */
            return true
        }

        val keys = xteaService?.get(id) ?: XteaKeyService.EMPTY_KEYS
        try {
            val landData = CacheManager.cache.data(MAPS, "l${x}_$z", keys) ?: return false

            loadLocations(landData) { loc ->
                val tile =
                    Tile(
                        baseX + loc.localX,
                        baseY + loc.localY,
                        loc.height,
                    )
                val hasBridge = bridges.contains(tile)
                if (hasBridge && loc.height == 0) return@loadLocations
                val adjustedTile = if (bridges.contains(tile)) tile.transform(-1) else tile
                val obj = StaticObject(loc.id, loc.type, loc.orientation, adjustedTile)
                world.chunks.getOrCreate(adjustedTile).addEntity(world, obj, adjustedTile)
            }
            return true
        } catch (e: IOException) {
            logger.error { "${"Could not decrypt map region {}."} $id" }
            return false
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
