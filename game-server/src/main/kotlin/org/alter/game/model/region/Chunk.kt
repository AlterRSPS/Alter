package org.alter.game.model.region

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import net.rsprot.protocol.api.util.ZonePartialEnclosedCacheBuffer
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.game.outgoing.zone.header.UpdateZonePartialEnclosed
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.*
import org.alter.game.model.collision.addLoc
import org.alter.game.model.collision.removeLoc
import org.alter.game.model.entity.*
import org.alter.game.model.region.update.*

/**
 * Represents an 8x8 tile in the game map.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Chunk(val coords: ChunkCoords) {

    internal val blockedTiles = ObjectOpenHashSet<Tile>()
    /**
     * The [Entity]s that are currently registered to the [Tile] key. This is
     * not used for [org.alter.game.model.entity.Pawn], but rather [Entity]s
     * that do not regularly change [Tile]s.
     */
    private lateinit var entities: MutableMap<Tile, MutableList<Entity>>
    /**
     * A list of [EntityUpdate]s that will be sent to players who have just entered
     * a region that has this chunk as viewable.
     */
    private lateinit var updates: MutableList<EntityUpdate<*>>
    /**
     * The [Npcs]s that are currently registered to the [Chunk] key.
     */
    lateinit var npcCollection: ObjectArrayList<Npc>
    lateinit var objectCollection: ObjectArrayList<GameObject>
    lateinit var playersCollection: ObjectArrayList<Player>
    lateinit var tempUpdates: ObjectArrayList<ZoneProt>

    /**
     * Create the collections used for [Entity]s and [EntityUpdate]s.
     * @see entities
     * @see updates
     */
    fun createEntityContainers() {
        entities = Object2ObjectOpenHashMap()
        updates = ObjectArrayList()
        npcCollection = ObjectArrayList()
    }

    /**
     * Check if [tile] belongs to this chunk.
     */
    fun contains(tile: Tile): Boolean = coords == tile.chunkCoords

    fun addEntity(
        world: World,
        entity: Entity,
        tile: Tile,
    ) {
        /*
         * Objects will affect the collision map.
         */
        if (entity.entityType.isObject) {
            val obj = entity as GameObject
            world.collision.addLoc(obj, obj.getDef())
        }

        /*
         * Transient entities will <strong>not</strong> be registered to one of
         * our [Chunk]'s tiles.
         */
        if (!entity.entityType.isTransient) {
            val list = entities[tile] ?: ObjectArrayList(1)
            list.add(entity)
            entities[tile] = list
        }

        /*
         * Create an [EntityUpdate] for our local players to receive and view.
         */
        val update = createUpdateFor(entity, spawn = true)
        if (update != null) {
            /*
             * [EntityType.STATIC_OBJECT]s shouldn't be sent to local players
             * for them to view since the client is already aware of them as
             * they are loaded from the game resources (cache).
             */
            if (entity.entityType != EntityType.STATIC_OBJECT) {
                /*
                 * [EntityType]s marked as transient will only be sent to local
                 * players who are currently in the viewport, but will now be
                 * sent to players who enter the region later on.
                 */
                if (!entity.entityType.isTransient) {
                    updates.add(update)
                }
                /*
                 * Send the update to all players in viewport.
                 */
                sendUpdate(world, update)
            }
        }
    }

    fun removeEntity(
        world: World,
        entity: Entity,
        tile: Tile,
    ) {
        /*
         * Transient entities do not get added to our [Chunk]'s tiles, so no use
         * in trying to remove it.
         */
        check(!entity.entityType.isTransient) { "Transient entities cannot be removed from chunks." }

        /*
         * [EntityType]s that are considered objects will be removed from our
         * collision map.
         */
        if (entity.entityType.isObject) {
            val obj = entity as GameObject
            world.collision.removeLoc(obj, obj.getDef())
        }

        entities[tile]?.remove(entity)

        /*
         * Create an [EntityUpdate] for our local players to receive and view.
         */
        val update = createUpdateFor(entity, spawn = false)
        if (update != null) {
            /*
             * If the entity is an [EntityType.STATIC_OBJECT], we want to cache
             * an [EntityUpdate] that will remove the entity when new players come
             * into this [Chunk]'s viewport.
             *
             * This is done because the client will always load [EntityType.STATIC_OBJECT]
             * through the game resources and have to be removed manually by our server.
             */
            if (entity.entityType == EntityType.STATIC_OBJECT) {
                updates.add(update)
            } else {
                updates.removeIf { it.entity == entity }
            }

            /*
             * Send the update to all players in viewport.
             */
            sendUpdate(world, update)
        }
    }

    /**
     * Update the item amount of an existing [GroundItem] in [entities].
     */
    fun updateGroundItem(
        world: World,
        item: GroundItem,
        oldAmount: Int,
        newAmount: Int,
    ) {
        val update = ObjCountUpdate(item, oldAmount, newAmount)
        sendUpdate(world, update)

        if (updates.removeIf { it.entity == item }) {
            updates.add(createUpdateFor(item, spawn = true)!!)
        }
    }

    val zonePartialEnclosedCacheBuffer: ZonePartialEnclosedCacheBuffer = ZonePartialEnclosedCacheBuffer()

    /**
     * Sends all [updates] from this chunk to the player [p].
     *
     */
    fun sendUpdates(p: Player) {
        val messages = ObjectArrayList<ZoneProt>()

        updates.forEach { update ->
            if (canBeViewed(p, update.entity)) {
                messages.add(update.toMessage())
            }
        }
        val local = p.lastKnownRegionBase!!.toLocal(coords.toTile())
        val computed = zonePartialEnclosedCacheBuffer.computeZone(messages)
        if (messages.isNotEmpty()) {
            p.write(UpdateZonePartialEnclosed(zoneX = local.x, zoneZ = local.z, level = local.height, payload = computed[OldSchoolClientType.DESKTOP]!!))
        }
    }

    /**
     * Checks to see if player [p] is able to view [entity].
     */
    private fun canBeViewed(
        p: Player,
        entity: Entity,
    ): Boolean {
        if (p.tile.height != entity.tile.height) {
            return false
        }
        if (entity.entityType.isGroundItem) {
            val item = entity as GroundItem
            return item.isPublic() || item.isOwnedBy(p)
        }
        return true
    }

    private fun <T : Entity> createUpdateFor(
        entity: T,
        spawn: Boolean,
    ): EntityUpdate<*>? =
        when (entity.entityType) {
            EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT ->
                if (spawn) {
                    LocAddChangeUpdate(entity as GameObject)
                } else {
                    LocDelUpdate(entity as GameObject)
                }

            EntityType.GROUND_ITEM ->
                if (spawn) {
                    ObjAddUpdate(entity as GroundItem)
                } else {
                    ObjDelUpdate(entity as GroundItem)
                }

        EntityType.PROJECTILE ->
            if (spawn) MapProjAnimUpdate(entity as Projectile)
            else throw RuntimeException("${entity.entityType} can only be spawned, not removed!")

            EntityType.AREA_SOUND ->
                if (spawn) {
                    SoundAreaUpdate(entity as AreaSound)
                } else {
                    throw RuntimeException("${entity.entityType} can only be spawned, not removed!")
                }

            EntityType.MAP_ANIM ->
                if (spawn) {
                    MapAnimUpdate(entity as TileGraphic)
                } else {
                    throw RuntimeException("${entity.entityType} can only be spawned, not removed!")
                }

            else -> null
        }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(vararg types: EntityType): List<T> = entities.values.flatten().filter { it.entityType in types } as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(
        tile: Tile,
        vararg types: EntityType,
    ): List<T> = entities[tile]?.filter { it.entityType in types } as? List<T> ?: emptyList()

    /**
     * Send the [update] to any [Client] entities that are within view distance
     * of this chunk.
     */
    private fun sendUpdate(
        world: World,
        update: EntityUpdate<*>,
    ) {
        val surrounding = coords.getSurroundingCoords()
        for (coords in surrounding) {
            val chunk = world.chunks.get(coords, createIfNeeded = false) ?: continue
            val clients = chunk.getEntities<Client>(EntityType.CLIENT)
            for (client in clients) {
                if (!canBeViewed(client, update.entity)) {
                    continue
                }
                val local = client.lastKnownRegionBase!!.toLocal(this.coords.toTile())
                val messages = ObjectArrayList<ZoneProt>()
                messages.add(update.toMessage())
                val computed = zonePartialEnclosedCacheBuffer.computeZone(messages)
                if (messages.isNotEmpty()) {
                    // Was not added from here MAP_PROJECTILE @TODO
                    client.write(UpdateZonePartialEnclosed(zoneX = local.x, zoneZ = local.z, level = local.height, payload = computed[OldSchoolClientType.DESKTOP]!!))
                }
            }
        }
    }
    companion object {
        /**
         * The size of a chunk, in tiles.
         */
        const val CHUNK_SIZE = 8

        /**
         * The amount of chunks in a region.
         */
        const val CHUNKS_PER_REGION = 13

        /**
         * The amount of [Chunk]s that can be viewed at a time by a player.
         */
        const val CHUNK_VIEW_RADIUS = 3

        /**
         * The size of a region, in tiles.
         */
        const val REGION_SIZE = CHUNK_SIZE * CHUNK_SIZE

        /**
         * The size of the viewport a [org.alter.game.model.entity.Player] can
         * 'see' at a time, in tiles.
         */
        const val MAX_VIEWPORT = CHUNK_SIZE * CHUNKS_PER_REGION
    }
}
