package dev.openrune.cache

import dev.openrune.cache.filestore.Cache
import dev.openrune.cache.filestore.definition.data.*
import dev.openrune.cache.filestore.definition.decoder.*
import java.nio.file.Path

object CacheManager {

    lateinit var cache: Cache
    var cacheRevision = -1

    private val npcs = mutableMapOf<Int, NpcType>()
    private val objects = mutableMapOf<Int, ObjectType>()
    private val items = mutableMapOf<Int, ItemType>()
    private val varbits = mutableMapOf<Int, VarBitType>()
    private val varps = mutableMapOf<Int, VarpType>()
    private val anims = mutableMapOf<Int, SequenceType>()
    private val enums = mutableMapOf<Int, EnumType>()
    private val healthBars = mutableMapOf<Int, HealthBarType>()
    private val hitsplats = mutableMapOf<Int, HitSplatType>()
    private val structs = mutableMapOf<Int, StructType>()
    private val dbtables = mutableMapOf<Int, DBTableType>()
    private val dbrows = mutableMapOf<Int, DBRowType>()

    fun init(cachePath: Path, cacheRevision: Int) {
        init(Cache.load(cachePath, false), cacheRevision)
    }

    @JvmStatic
    fun init(cache: Cache, cacheRevision: Int) {
        this.cacheRevision = cacheRevision
        this.cache = cache
        npcs.putAll(NPCDecoder().load(cache))
        objects.putAll(ObjectDecoder().load(cache))
        items.putAll(ItemDecoder().load(cache))
        varbits.putAll(VarBitDecoder().load(cache))
        varps.putAll(VarDecoder().load(cache))
        anims.putAll(SequenceDecoder().load(cache))
        enums.putAll(EnumDecoder().load(cache))
        healthBars.putAll(HealthBarDecoder().load(cache))
        hitsplats.putAll(HitSplatDecoder().load(cache))
        structs.putAll(StructDecoder().load(cache))
        dbtables.putAll(DBTableDecoder().load(cache))
        dbrows.putAll(DBRowDecoder().load(cache))
    }

    private inline fun <T> getOrDefault(map: Map<Int, T>, id: Int, default: T, typeName: String): T {
        return map.getOrDefault(id, default).also {
            if (id == -1) println("$typeName with id $id is missing.")
        }
    }

    fun getNpc(id: Int) = npcs[id] ?: throw IllegalStateException("Could not find Npc with id: $id")
    fun getObject(id: Int) = objects[id] ?: throw IllegalStateException("Could not find Object with id: $id")
    fun getItem(id: Int) = items[id] ?: throw IllegalStateException("Could not find Item with id: $id")
    fun getVarbit(id: Int) = varbits[id] ?: throw IllegalStateException("Could not find Varbit with id: $id")
    fun getVarp(id: Int) = varps[id] ?: throw IllegalStateException("Could not find Varp with id: $id")
    fun getAnim(id: Int) = anims[id] ?: throw IllegalStateException("Could not find Anim with id: $id")
    fun getEnum(id: Int) = enums[id] ?: throw IllegalStateException("Could not find Enum with id: $id")
    fun getHealthBar(id: Int) = healthBars[id] ?: throw IllegalStateException("Could not find HealthBar with id: $id")
    fun getHitsplat(id: Int) = hitsplats[id] ?: throw IllegalStateException("Could not find Hitsplat with id: $id")
    fun getStruct(id: Int) = structs[id] ?: throw IllegalStateException("Could not find Struct with id: $id")
    fun getDBTable(id: Int) = dbtables[id] ?: throw IllegalStateException("Could not find DBTable with id: $id")
    fun getDBRow(id: Int) = dbrows[id] ?: throw IllegalStateException("Could not find DBRow with id: $id")
    fun getNpcOrDefault(id: Int) = getOrDefault(npcs, id, NpcType(), "Npc")
    fun getObjectOrDefault(id: Int) = getOrDefault(objects, id, ObjectType(), "Object")
    fun getItemOrDefault(id: Int) = getOrDefault(items, id, ItemType(), "Item")
    fun getVarbitOrDefault(id: Int) = getOrDefault(varbits, id, VarBitType(), "Varbit")
    fun getVarpOrDefault(id: Int) = getOrDefault(varps, id, VarpType(), "Varp")
    fun getAnimOrDefault(id: Int) = getOrDefault(anims, id, SequenceType(), "Anim")
    fun getEnumOrDefault(id: Int) = getOrDefault(enums, id, EnumType(), "Enum")
    fun getHealthBarOrDefault(id: Int) = getOrDefault(healthBars, id, HealthBarType(), "HealthBar")
    fun getHitsplatOrDefault(id: Int) = getOrDefault(hitsplats, id, HitSplatType(), "Hitsplat")
    fun getStructOrDefault(id: Int) = getOrDefault(structs, id, StructType(), "Struct")

    fun findScriptId(name: String): Int {
        val cacheName = "[clientscript,$name]"
        return cache.archiveId(CLIENTSCRIPT, cacheName).also { id ->
            if (id == -1) println("Unable to find script: $cacheName")
        }
    }

    // Size methods
    fun npcSize() = npcs.size
    fun objectSize() = objects.size
    fun itemSize() = items.size
    fun varbitSize() = varbits.size
    fun varpSize() = varps.size
    fun animSize() = anims.size
    fun enumSize() = enums.size
    fun healthBarSize() = healthBars.size
    fun hitsplatSize() = hitsplats.size
    fun structSize() = structs.size

    // Bulk getters
    fun getNpcs() = npcs.toMap()
    fun getObjects() = objects.toMap()
    fun getItems() = items.toMap()
    fun getVarbits() = varbits.toMap()
    fun getVarps() = varps.toMap()
    fun getAnims() = anims.toMap()
    fun getEnums() = enums.toMap()
    fun getHealthBars() = healthBars.toMap()
    fun getHitsplats() = hitsplats.toMap()
    fun getStructs() = structs.toMap()

    // Cache revision methods
    fun revisionIsOrAfter(rev: Int) = rev <= cacheRevision
    fun revisionIsOrBefore(rev: Int) = rev >= cacheRevision
    
}
