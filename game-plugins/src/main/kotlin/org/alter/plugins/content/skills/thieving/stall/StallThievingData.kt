package org.alter.plugins.content.skills.thieving.stall

/**
 * Immutable configuration representing a thievable stall loaded from JSON.
 */
data class StallEntry(
    val objects: List<String>,
    val emptyObject: String,
    val respawnTicks: Int = 10,
    val level: Int,
    val experience: Double,
    val loot: List<StallLoot>,
) {
    @Transient
    var objectIds: IntArray = intArrayOf()

    @Transient
    var emptyObjectId: Int = -1

    init {
        require(objects.isNotEmpty()) { "Stall entry must define at least one object id." }
        require(emptyObject.isNotBlank()) { "Stall entry must define the empty stall object id." }
        require(respawnTicks >= 1) { "Stall respawn ticks must be at least 1." }
        require(level >= 1) { "Stall level requirement must be >= 1." }
        require(experience >= 0.0) { "Stall experience cannot be negative." }
        require(loot.isNotEmpty()) { "Stall entry must define at least one loot drop." }
    }
}

data class StallLoot(
    val item: String,
    val min: Int = 1,
    val max: Int = min,
    /**
     * Weight used when rolling the loot table for this stall.
     */
    val weight: Double = 1.0,
) {
    init {
        require(item.isNotBlank()) { "Loot item id cannot be blank." }
        require(min >= 0) { "Loot minimum amount cannot be negative." }
        require(max >= min) { "Loot maximum cannot be less than minimum." }
        require(weight > 0.0) { "Loot weight must be greater than 0." }
    }
}
