package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.filestore.definition.Parameterized
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

data class StructType(
    override var id: Int = -1,
    override var inherit: Int = -1,
    override var params: Map<Int, Any>? = Int2ObjectOpenHashMap()
) : Definition, Parameterized {

    fun getInt(key: Int): Int = params?.get(key) as? Int ?: -1

    fun getString(key: Int): String = params?.get(key) as? String ?: ""

    companion object {
        val EMPTY = StructType()
    }
}