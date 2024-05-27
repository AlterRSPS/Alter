package org.alter.game.fs

import com.displee.cache.CacheLibrary
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.fs.def.*
import org.alter.game.model.Direction
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.collision.CollisionUpdate
import org.alter.game.model.entity.StaticObject
import org.alter.game.model.region.ChunkSet
import org.alter.game.service.xtea.XteaKeyService
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.io.IOException

/**
 * A [DefinitionSet] is responsible for loading any relevant metadata found in
 * the game resources.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DefinitionSet {

    /**
     * A [Map] holding all definitions with their [Class] as key.
     */
    private val defs = Object2ObjectOpenHashMap<Class<out Definition>, Map<Int, *>>()

    private var xteaService: XteaKeyService? = null

    fun loadAll(library: CacheLibrary) {
        /*
         * Load [AnimDef]s.
         */
        load(library, AnimDef::class.java)
        logger.info("Loaded ${getCount(AnimDef::class.java)} animation definitions.")

        /*
         * Load [VarpDef]s.
         */
        load(library, VarpDef::class.java)
        logger.info("Loaded ${getCount(VarpDef::class.java)} varp definitions.")

        /*
         * Load [VarbitDef]s.
         */
        load(library, VarbitDef::class.java)
        logger.info("Loaded ${getCount(VarbitDef::class.java)} varbit definitions.")

        /*
         * Load [EnumDef]s.
         */
        load(library, EnumDef::class.java)
        logger.info("Loaded ${getCount(EnumDef::class.java)} enum definitions.")

        /*
         * Load [NpcDef]s.
         */
        load(library, NpcDef::class.java)
        logger.info("Loaded ${getCount(NpcDef::class.java)} npc definitions.")

        /*
         * Load [ItemDef]s.
         */
        load(library, ItemDef::class.java)
        logger.info("Loaded ${getCount(ItemDef::class.java)} item definitions.")

        /*
         * Load [ObjectDef]s.
         */
        load(library, ObjectDef::class.java)
        logger.info("Loaded ${getCount(ObjectDef::class.java)} object definitions.")
    }

    fun loadRegions(world: World, chunks: ChunkSet, regions: IntArray) {
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

    fun <T : Definition> load(library: CacheLibrary, type: Class<out T>) {
        val configType: ConfigType = when (type) {
            VarpDef::class.java -> ConfigType.VARPLAYER
            VarbitDef::class.java -> ConfigType.VARBIT
            EnumDef::class.java -> ConfigType.ENUM
            NpcDef::class.java -> ConfigType.NPC
            ObjectDef::class.java -> ConfigType.OBJECT
            ItemDef::class.java -> ConfigType.ITEM
            AnimDef::class.java -> ConfigType.SEQUENCE
            else -> throw IllegalArgumentException("Unhandled class type ${type::class.java}.")
        }
        val configs = library.index(IndexType.CONFIGS.number)
        val archive = configs.archive(configType.id)
        val files = archive!!.files

        val definitions = Int2ObjectOpenHashMap<T?>(files.size + 1)

        for (i in 0 until files.size) {
            val file = files[i] ?: continue
            val data = file.data ?: continue
            val def = createDefinition(type, file.id, data)
            definitions[file.id] = def
        }
        defs[type] = definitions
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Definition> createDefinition(type: Class<out T>, id: Int, data: ByteArray): T {
        val def: Definition = when (type) {
            VarpDef::class.java -> VarpDef(id)
            VarbitDef::class.java -> VarbitDef(id)
            EnumDef::class.java -> EnumDef(id)
            NpcDef::class.java -> NpcDef(id)
            ObjectDef::class.java -> ObjectDef(id)
            ItemDef::class.java -> ItemDef(id)
            AnimDef::class.java -> AnimDef(id)
            else -> throw IllegalArgumentException("Unhandled class type ${type::class.java}.")
        }

        val buf = Unpooled.wrappedBuffer(data)
        def.decode(buf)
        buf.release()
        return def as T
    }

    fun getCount(type: Class<*>) = defs[type]!!.size

    @Suppress("UNCHECKED_CAST")
    fun <T : Definition> get(type: Class<out T>, id: Int): T {
        return (defs[type]!!)[id] as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Definition> getNullable(type: Class<out T>, id: Int): T? {
        return (defs[type]!!)[id] as T?
    }

    /**
     * Creates an 8x8 [gg.rsmod.game.model.region.Chunk] region.
     */
    fun createRegion(world: World, id: Int): Boolean {
        if (xteaService == null) {
            xteaService = world.getService(XteaKeyService::class.java)
        }

        val x = id shr 8
        val z = id and 0xFF

        val mapData = world.filestore.data(IndexType.MAPS.number, "m${x}_$z") ?: return false

        val baseX: Int = id shr 8 and 255 shl 6
        val baseY: Int = id and 255 shl 6

        val blocked = hashSetOf<Tile>()
        val bridges = hashSetOf<Tile>()

        val tiles = loadTerrain(mapData)

        for (height in 0 until 4) {
            for (lx in 0 until 64) {
                for (lz in 0 until 64) {
                    val bridge = tiles[1][lx][lz]!!.settings.toInt() and 0x2 != 0
                    if (bridge) {
                        bridges.add(Tile(baseX + lx, baseY + lz, height))
                    }
                    val blockedTile = tiles[height][lx][lz]!!.settings.toInt() and 0x1 != 0
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
        }
        world.collision.applyUpdate(blockedTileBuilder.build())

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
            val landData = world.filestore.data(IndexType.MAPS.number, "l${x}_$z", keys) ?: return false

            loadLocations(landData).forEach { loc ->
                val tile = Tile(
                    baseX + loc.position.x,
                    baseY + loc.position.y,
                    loc.position.z
                )
                val hasBridge = bridges.contains(tile)
                if (hasBridge && loc.position.z == 0) return@forEach
                val adjustedTile = if (bridges.contains(tile)) tile.transform(-1) else tile
                val obj = StaticObject(loc.id, loc.type, loc.orientation, adjustedTile)
                world.chunks.getOrCreate(adjustedTile).addEntity(world, obj, adjustedTile)
            }
            return true
        } catch (e: IOException) {
            logger.error("Could not decrypt map region {}.", id)
            return false
        }
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}