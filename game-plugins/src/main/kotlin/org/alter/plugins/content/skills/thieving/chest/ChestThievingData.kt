package org.alter.plugins.content.skills.thieving.chest

/**
 * Immutable configuration representing a thievable chest loaded from JSON.
 */
data class ChestEntry(
    val objects: List<String>,
    val openObject: String,
    val respawnTicks: Int = 10,
    val level: Int,
    val experience: Double,
    val loot: List<ChestLoot>,
    val trap: ChestTrap? = null,
) {
    @Transient
    var objectIds: IntArray = intArrayOf()

    @Transient
    var openObjectId: Int = -1

    init {
        require(objects.isNotEmpty()) { "Chest entry must define at least one object id." }
        require(openObject.isNotBlank()) { "Chest entry must define the open chest object id." }
        require(respawnTicks >= 1) { "Chest respawn ticks must be at least 1." }
        require(level >= 1) { "Chest level requirement must be >= 1." }
        require(experience >= 0.0) { "Chest experience cannot be negative." }
        require(loot.isNotEmpty()) { "Chest entry must define at least one loot drop." }
        trap?.let { trapCfg ->
            if (trapCfg.enabled) {
                require(trapCfg.dismantleChance in 0..100) { "Trap dismantle chance must be between 0 and 100." }
                require(trapCfg.minDamage >= 0) { "Trap minimum damage cannot be negative." }
                require(trapCfg.maxDamage >= trapCfg.minDamage) { "Trap maximum damage cannot be less than minimum." }
            }
        }
    }
}

data class ChestLoot(
    val item: String,
    val min: Int = 1,
    val max: Int = min,
) {
    init {
        require(item.isNotBlank()) { "Loot item id cannot be blank." }
        require(min >= 0) { "Loot minimum amount cannot be negative." }
        require(max >= min) { "Loot maximum cannot be less than minimum." }
    }
}

data class ChestTrap(
    val enabled: Boolean = false,
    val dismantleChance: Int = 0,
    val minDamage: Int = 0,
    val maxDamage: Int = minDamage,
)
