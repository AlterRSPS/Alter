package gg.rsmod.game.fs

import gg.rsmod.game.fs.def.EnumDef
import gg.rsmod.game.fs.def.StructDef
import gg.rsmod.game.model.World
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class EnumDefinitions(id : Int, world : World) {

    private var values = Int2ObjectOpenHashMap<Any>()

    private var enumId = id

    private val world = world

    fun get() : EnumDefinitions? {
        val struct = world.definitions.getNullable(EnumDef::class.java, enumId)

        if (struct != null) {
            values = struct.values
        }

        return this
    }

    fun getId() : Int {
        return enumId
    }

    fun getValueAsString(value: Int) : String {
        return values[value].toString()
    }

    fun getValueAsInt(value : Int) : Int {
        return values[value] as Int
    }

    fun getValueAsBoolean(value: Int) : Boolean {
        return values[value] as Boolean
    }

    fun getValues() : Int2ObjectOpenHashMap<Any> {
        return values
    }
}