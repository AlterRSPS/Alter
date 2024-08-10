package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

data class EnumType(
    override var id: Int = -1,
    var keyType : Int = 0,
    var valueType : Int = 0,
    var defaultInt : Int = 0,
    var defaultString : String = "",
    val values : Int2ObjectOpenHashMap<Any> = Int2ObjectOpenHashMap<Any>(),
    //Custom
    override var inherit: Int = -1
) : Definition {

    fun getSize() = values.size

    fun getInt(key: Int): Int = values.get(key) as? Int ?: defaultInt

    fun getString(key: Int): String = values.get(key) as? String ?: defaultString
}