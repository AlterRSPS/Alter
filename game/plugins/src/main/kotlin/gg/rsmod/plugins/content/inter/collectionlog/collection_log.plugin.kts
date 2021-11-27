package gg.rsmod.plugins.content.inter.collectionlog

val COLLECTION_LOG_INTERFACE = 621

val STRUCT_OFFSET = 471
val ENUM_OFFSET = 2103

CollectionTabs.values().forEach { tab ->
    on_button(COLLECTION_LOG_INTERFACE, 4 + tab.ordinal) {
        player.runClientScript(2389, tab.ordinal)
    }

    val categories = world.definitions.get(EnumDef::class.java, ENUM_OFFSET + tab.ordinal).values.values.toList()
    //println("${tab.name}: ")

    @Suppress("UNCHECKED_CAST")
    (categories as? List<Int>)?.forEach { cat ->
        val logs = world.definitions.get(StructDef::class.java, cat).params.values.toList()

        /**
         * @TODO
         */
        //print("category($cat): ${logs[0]} - ")

        val items = world.definitions.get(EnumDef::class.java, logs[1] as Int).values.values.toList()
        //println(items)

        /**
         * Initializes the default [Unlocks.UNLOCK_MAP] [ByteArray]
         * sizes for tracking unlocks
         */
        CollectionTabs.UNLOCK_MAP[cat] = BooleanArray(items.size)
    }

}

val COLLECTION_UNLOCKS = AttributeKey<Map<Int, BooleanArray>>("collection_unlocks")

//on_login {
//    player.attr.getOrDefault(COLLECTION_UNLOCKS, CollectionTabs.UNLOCK_MAP)
//    player.attr[COLLECTION_UNLOCKS]?.get(476)?.set(0, true)
//}

on_button(COLLECTION_LOG_INTERFACE, 11) {
    player.message("clicked slot ${player.getInteractingSlot()}")
}

