package gg.rsmod.game.fs

import gg.rsmod.game.fs.def.StructDef
import gg.rsmod.game.model.World
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

    fun getId() : Int {
        return structId
    }

    fun getParamAsString(param: Int) : String {
        return params[param].toString()
    }

    fun getParamAsInt(param : Int) : Int {
        return params[param] as Int
    }

    fun getParamAsBoolean(param: Int) : Boolean {
        return params[param] as Boolean
    }
}