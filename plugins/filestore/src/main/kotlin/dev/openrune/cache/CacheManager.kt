package dev.openrune.cache

import dev.openrune.cache.filestore.Cache
import dev.openrune.cache.filestore.definition.data.*
import dev.openrune.cache.filestore.definition.decoder.*
import java.nio.file.Path

object CacheManager {

    lateinit var cache: Cache

    private val npcs: MutableMap<Int, NpcType> = mutableMapOf()
    private val objects: MutableMap<Int, ObjectType> = mutableMapOf()
    private val items: MutableMap<Int, ItemType> = mutableMapOf()
    private val varbits: MutableMap<Int, VarBitType> = mutableMapOf()
    private val varps: MutableMap<Int, VarpType> = mutableMapOf()
    private val anims: MutableMap<Int, SequenceType> = mutableMapOf()
    private val enums: MutableMap<Int, EnumType> = mutableMapOf()
    private val healthBars: MutableMap<Int, HealthBarType> = mutableMapOf()
    private val hitsplats: MutableMap<Int, HitSplatType> = mutableMapOf()
    private val structs: MutableMap<Int, StructType> = mutableMapOf()

    private var cacheRevision = -1


    fun init(cachePath: Path, cacheRevision : Int) {
        init(Cache.load(cachePath, false), cacheRevision)
    }

    fun init(cache: Cache, cacheRevision : Int) {
        this.cacheRevision = cacheRevision;
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
    }


    fun getNpc(id: Int): NpcType {
        return npcs.getOrDefault(id, NpcType(id)).also {
            if (it.id != id) println("Npc with id $id is missing.")
        }
    }

    fun getObject(id: Int): ObjectType {
        return objects.getOrDefault(id, ObjectType(id)).also {
            if (it.id != id) println("Object with id $id is missing.")
        }
    }

    fun getItem(id: Int): ItemType {
        return items.getOrDefault(id, ItemType(id)).also {
            if (it.id != id) println("Item with id $id is missing.")
        }
    }

    fun getVarbit(id: Int): VarBitType {
        return varbits.getOrDefault(id, VarBitType(id)).also {
            if (it.id != id) println("Varbit with id $id is missing.")
        }
    }

    fun getVarp(id: Int): VarpType {
        return varps.getOrDefault(id, VarpType(id)).also {
            if (it.id != id) println("Varp with id $id is missing.")
        }
    }

    fun getAnim(id: Int): SequenceType {
        return anims.getOrDefault(id, SequenceType(id)).also {
            if (it.id != id) println("Anim with id $id is missing.")
        }
    }

    fun getEnum(id: Int): EnumType {
        return enums.getOrDefault(id, EnumType(id)).also {
            if (it.id != id) println("Enum with id $id is missing.")
        }
    }

    fun getHealthBar(id: Int): HealthBarType {
        return healthBars.getOrDefault(id, HealthBarType(id)).also {
            if (it.id != id) println("HealthBar with id $id is missing.")
        }
    }

    fun getHitsplat(id: Int): HitSplatType {
        return hitsplats.getOrDefault(id, HitSplatType(id)).also {
            if (it.id != id) println("Hitsplat with id $id is missing.")
        }
    }

    fun getStruct(id: Int): StructType {
        return structs.getOrDefault(id, StructType(id)).also {
            if (it.id != id) println("Struct with id $id is missing.")
        }
    }

    fun findScriptId(name: String): Int {
        val cacheName = "[clientscript,$name]"
        return cache.archiveId(CLIENTSCRIPT, cacheName).also { id ->
            if (id == -1) println("Unable to find script: $cacheName")
        }
    }

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

    fun getNpcs(): Map<Int, NpcType> = npcs
    fun getObjects(): Map<Int, ObjectType> = objects
    fun getItems(): Map<Int, ItemType> = items
    fun getVarbits(): Map<Int, VarBitType> = varbits
    fun getVarps(): Map<Int, VarpType> = varps
    fun getAnims(): Map<Int, SequenceType> = anims
    fun getEnums(): Map<Int, EnumType> = enums
    fun getHealthBars(): Map<Int, HealthBarType> = healthBars
    fun getHitsplats(): Map<Int, HitSplatType> = hitsplats
    fun getStructs(): Map<Int, StructType> = structs

    fun revisionIsOrAfter(rev : Int) = rev <= cacheRevision
    fun revisionIsOrBefore(rev : Int) = rev >= cacheRevision


}
