package gg.rsmod.plugins.content.inter.collectionlog

enum class CollectionTabs {
    BOSSES,
    RAIDS,
    CLUES,
    MINIGAMES,
    OTHER;

    companion object Unlocks {
        val UNLOCK_MAP = mutableMapOf<Int, BooleanArray>()
    }
}