package org.alter.plugins.content.items.ringofwealth

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.canTeleport
import org.alter.plugins.content.magic.teleport
import org.alter.rscm.RSCM.getRSCM

class RingOfWealthPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    val RING_OF_WEALTH =
        arrayOf(
            "item.ring_of_wealth_1",
            "item.ring_of_wealth_2",
            "item.ring_of_wealth_3",
            "item.ring_of_wealth_4",
            "item.ring_of_wealth_5",
        )

    private val SOUNDAREA_ID = 200
    private val SOUNDAREA_RADIUS = 5
    private val SOUNDAREA_VOLUME = 1

    private val LOCATIONS =
        mapOf(
            "Miscellania" to Tile(2537, 3875),
            "Grand Exchange" to Tile(3164, 3481),
            "Falador" to Tile(3004, 3361),
            "Dondakan" to Tile(3004, 3361),
        )

    private val OPTIONS =
        mapOf(
            "Boss Log" to Tile(3004, 3361),
            "Coin Collection" to Tile(3004, 3361),
        )

    init {
        RING_OF_WEALTH.forEach { wealth ->
            LOCATIONS.forEach { location, tile ->
                onEquipmentOption(wealth, option = location) {
                    player.queue(TaskPriority.STRONG) {
                        player.teleport(tile)
                    }
                }
            }
            OPTIONS.forEach { options, open ->
                onEquipmentOption(wealth, option = options) {
                    player.queue(TaskPriority.STRONG) {
                        player.message("Placeholder for both boss log and coin collector. mapof($open)", ChatMessageType.ENGINE)
                    }
                }
            }
        }
    }


    fun Player.teleport(endTile: Tile) {
        if (canTeleport(TeleportType.GLORY) && hasEquipped(EquipmentType.RING, *RING_OF_WEALTH)) {
            world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
            equipment[EquipmentType.RING.id] = getRingReplacement()
            message(getRingChargeReplacement())
            teleport(endTile, TeleportType.GLORY)
        }
    }

    fun Player.getRingReplacement(): Item? {
        return when {
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_5") -> Item(getRSCM("item.ring_of_wealth_4"))
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_4") -> Item(getRSCM("item.ring_of_wealth_3"))
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_3") -> Item(getRSCM("item.ring_of_wealth_2"))
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_2") -> Item(getRSCM("item.ring_of_wealth_1"))
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_1") -> Item(getRSCM("item.ring_of_wealth"))
            else -> null
        }
    }

    fun Player.getRingChargeReplacement(): String {
        return when {
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_4") -> "<col=7f007f>Your ring has four charges left.</col>"
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_3") -> "<col=7f007f>Your ring has three charges left.</col>"
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_2") -> "<col=7f007f>Your ring has two charges left.</col>"
            hasEquipped(EquipmentType.RING, "item.ring_of_wealth_1") -> "<col=7f007f>Your ring has one charge left.</col>"
            else -> "<col=7f007f>You use your ring's last charge.</col>"
        }
    }
}
