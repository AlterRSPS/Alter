package org.alter.game.fs

import org.alter.game.fs.def.StructDef
import org.alter.game.model.World
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class StructDefinitions(id : Int, world : World) {

    private var params = Int2ObjectOpenHashMap<Any>()

    private var structId = id

    private val world = world

    fun get() : StructDefinitions {
        val struct = world.definitions.getNullable(StructDef::class.java, structId)

        if (struct != null) {
            params = struct.params
        }

        return this
    }

    fun getAllParams() : Int2ObjectOpenHashMap<Any> {
        return params
    }

    fun getId() : Int {
        return structId
    }

    fun getParamAsString(param: Int) : String? {
        return params.getOrDefault(param, null) as String?
    }

    fun getParamAsInt(param : Int) : Int? {
        return params.getOrDefault(param, null) as Int?
    }

    fun getParamAsBoolean(param: Int) : Boolean {
        if(params.getOrDefault(param, "no") == "yes") {
            return true
        }

        return false
    }
}