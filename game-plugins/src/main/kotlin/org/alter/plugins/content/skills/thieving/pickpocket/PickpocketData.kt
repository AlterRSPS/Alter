package org.alter.plugins.content.skills.thieving.pickpocket

/**
 * Immutable configuration representing a pickpocket target loaded from JSON.
 */
data class PickpocketEntry(
    val npcs: List<String>,
    val level: Int,
    val experience: Double,
    val baseSuccess: Double = 0.55,
    val successBonusPerLevel: Double = 0.01,
    val stun: PickpocketStun,
    val loot: List<PickpocketLoot>,
) {
    init {
        require(npcs.isNotEmpty()) { "Pickpocket entry must define at least one npc id." }
        require(level >= 1) { "Pickpocket level requirement must be >= 1." }
        require(experience >= 0.0) { "Pickpocket experience cannot be negative." }
        require(baseSuccess in 0.0..1.0) { "Base success must be a value between 0.0 and 1.0." }
        require(successBonusPerLevel >= 0.0) { "Success bonus per level cannot be negative." }
        require(loot.isNotEmpty()) { "Pickpocket entry must define at least one loot drop." }
    }
}

data class PickpocketStun(
    val ticks: Int,
    val damage: DamageRange,
) {
    init {
        require(ticks >= 0) { "Stun ticks cannot be negative." }
    }
}

data class DamageRange(
    val min: Int,
    val max: Int,
) {
    init {
        require(min >= 0) { "Minimum stun damage cannot be negative." }
        require(max >= min) { "Maximum stun damage cannot be less than minimum." }
    }
}

data class PickpocketLoot(
    val item: String,
    val min: Int = 1,
    val max: Int = min,
    /**
     * Percentage chance (0.01 - 100.00) that this loot is selected when a pickpocket succeeds.
     */
    val weight: Double = 1.0,
) {
    init {
        require(item.isNotBlank()) { "Loot item id cannot be blank." }
        require(min >= 0) { "Loot minimum amount cannot be negative." }
        require(max >= min) { "Loot maximum cannot be less than minimum." }
        require(weight > 0.0 && weight <= 100.0) { "Loot weight must be between 0.0 (exclusive) and 100.0 (inclusive)." }
    }
}
