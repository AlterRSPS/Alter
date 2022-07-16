package gg.rsmod.plugins.content.items.ringofwealth

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport
import gg.rsmod.plugins.content.magic.teleport

val RING_OF_WEALTH = intArrayOf(
    Items.RING_OF_WEALTH_1, Items.RING_OF_WEALTH_2, Items.RING_OF_WEALTH_3, Items.RING_OF_WEALTH_4, Items.RING_OF_WEALTH_5
)

private val SOUNDAREA_ID = 200
private val SOUNDAREA_RADIUS = 5
private val SOUNDAREA_VOLUME = 1

private val LOCATIONS = mapOf(
    "Miscellania" to Tile(2537, 3875),
    "Grand Exchange" to Tile(3164, 3481),
    "Falador" to Tile(3004, 3361),
    "Dondakan" to Tile(3004, 3361)
)

private val OPTIONS = mapOf(
    "Boss Log" to Tile(3004, 3361),
    "Coin Collection" to Tile(3004, 3361)
)

RING_OF_WEALTH.forEach { wealth ->
    LOCATIONS.forEach { location, tile ->
        on_equipment_option(wealth, option = location) {
            player.queue(TaskPriority.STRONG) {
                player.teleport(tile)
            }
        }
    }
    OPTIONS.forEach { options, open ->
        on_equipment_option(wealth, option = options) {
            player.queue(TaskPriority.STRONG) {
                player.message("Placeholder for both boss log and coin collector. mapof(${open})" , ChatMessageType.ENGINE)
                }
            }
        }
    }

fun Player.teleport(endTile : Tile) {
    if (canTeleport(TeleportType.GLORY) && hasEquipped(EquipmentType.RING, *RING_OF_WEALTH)) {
        world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
        equipment[EquipmentType.RING.id] = getRingReplacement()
        message(getRingChargeReplacement())
        teleport(endTile, TeleportType.GLORY)
    }
}

fun Player.getRingReplacement(): Item ? {
    return when {
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_5) -> Item(Items.RING_OF_WEALTH_4)
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_4) -> Item(Items.RING_OF_WEALTH_3)
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_3) -> Item(Items.RING_OF_WEALTH_2)
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_2) -> Item(Items.RING_OF_WEALTH_1)
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_1) -> Item(Items.RING_OF_WEALTH)
        else -> null
    }
}

fun Player.getRingChargeReplacement(): String {
    return when {
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_4) -> "<col=7f007f>Your ring has four charges left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_3) -> "<col=7f007f>Your ring has three charges left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_2) -> "<col=7f007f>Your ring has two charges left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_WEALTH_1) -> "<col=7f007f>Your ring has one charge left.</col>"
        else -> "<col=7f007f>You use your ring's last charge.</col>"
    }
}