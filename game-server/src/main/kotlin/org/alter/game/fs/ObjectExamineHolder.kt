package org.alter.game.fs

import dev.openrune.cache.CacheManager.getItems
import dev.openrune.cache.CacheManager.itemSize
import dev.openrune.cache.CacheManager.objectSize
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * Created by Advo on 5/28/2024
 */
object ObjectExamineHolder {
    var EXAMINES: Int2ObjectMap<String> = Int2ObjectOpenHashMap(objectSize())
    var EQUIPMENT_MENU: Int2ObjectMap<ItemType2> = Int2ObjectOpenHashMap(itemSize())

    fun load() {
        getItems().entries.forEach {
            val type2 = ItemType2()
            val type = it.value
            EQUIPMENT_MENU.put(it.key, type2)
            if (type.params != null) {
                for (i in 0 until 8) {
                    val paramId = 451 + i
                    val option = type.params!!.get(paramId) as? String ?: continue
                    type2.equipmentMenu[i] = option
                }
            }
        }
    }
}
