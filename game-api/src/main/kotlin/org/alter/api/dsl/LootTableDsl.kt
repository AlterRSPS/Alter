package org.alter.api.dsl

import org.alter.game.model.Tile

/**
 * So we want to have class LootTable() which could be used rolled from Npc/Chests/Items
 */
object LootTableDsl {
    /**
    val table = ArrayList<>()

    @DslMarker
    annotation class LootTableDslMarker

    enum class Tables {
        PREROLL,
        MAIN,
        ALWAYS,
    }

    @LootTableDslMarker
    class Builder {
        /**
         * @Provide Where to drop the loot. -> If not set it will drop where the npc was.
         * -> If it's a Mystery Box / Chest -> Drop beneath the player
         */
        val position: Tile? = null

        /**
         * If it's true it will drop the item underneath the player.
         */
        val dropIfInvFull: Boolean = true

        fun position() {}
        fun add() {}
    }
    */
}
