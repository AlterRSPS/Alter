package org.alter.plugins.content.items.consumables.teletabs

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
import org.alter.game.model.move.moveTo
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.canTeleport
import org.alter.plugins.content.magic.prepareForTeleport
import org.alter.rscm.RSCM.getRSCM

class TeleportTabPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    private val LOCATIONS =
        mapOf(
            "item.varrock_teleport" to Area(3210, 3423, 3216, 3425),
            "item.falador_teleport" to Area(2961, 3376, 2969, 3385),
            "item.lumbridge_teleport" to Area(3221, 3218, 3222, 3219),
            "item.camelot_teleport" to Area(2756, 3476, 2758, 3480),
            "item.ardougne_teleport" to Area(2659, 3300, 2665, 3310),
            "item.watchtower_teleport" to Area(2551, 3113, 2553, 3116),
            "item.rimmington_teleport" to Area(2953, 3222, 2956, 3226),
            "item.taverley_teleport" to Area(2893, 3463, 2894, 3467),
            "item.pollnivneach_teleport" to Area(3338, 3003, 3342, 3004),
            "item.hosidius_teleport" to Area(1742, 3515, 1743, 3518),
            "item.rellekka_teleport" to Area(2668, 3631, 2671, 3632),
            "item.brimhaven_teleport" to Area(2757, 3176, 2758, 3179),
            "item.yanille_teleport" to Area(2542, 3095, 2545, 3096),
            "item.trollheim_teleport" to Area(2888, 3678, 2893, 3681),
            "item.catherby_teleport" to Area(2800, 3449, 2801, 3450),
            "item.barbarian_teleport" to Area(2543, 3570, 2544, 3571),
            // Items.LUMBRIDGE_GRAVEYARD_TELEPORT to Area(1632, 3839, 1633, 3840),
            "item.draynor_manor_teleport" to Area(3108, 3352, 3108, 3352),
            // Items.FELDIP_HILLS_TELEPORT to Area(2542, 2925, 2542, 2925), -> Teleport
            "item.fishing_guild_teleport" to Area(2612, 3391, 2612, 3391),
            "item.khazard_teleport" to Area(2637, 3166, 2637, 3166),
            "item.mind_altar_teleport" to Area(2979, 3509, 2979, 3509),
            // Items.LU
            // @TODO Items.APE_ATOLL_TELEPORT , Need to have Monkey Madness and Receive 10th Squad Training from Daero
        )

    init {
        LOCATIONS.forEach { item, endTile ->
            onItemOption(item = item, option = "break") {
                player.queue(TaskPriority.STRONG) {
                    player.teleport(this, endTile, getRSCM(item))
                }
            }
        }
    }

// Items.DIGSITE_TELEPORT to Tile(3324, 3411)
// Items.LUMBERYARD_TELEPORT to Tile(3302, 3488)

    suspend fun Player.teleport(
        it: QueueTask,
        endArea: Area,
        tab: Int,
    ) {
        if (canTeleport(TeleportType.MODERN) && inventory.contains(tab)) {
            inventory.remove(item = tab)
            prepareForTeleport()
            lock = LockState.FULL_WITH_DAMAGE_IMMUNITY
            animate(id = 4069, delay = 16)
            playSound(id = 965, volume = 1, delay = 15)
            it.wait(cycles = 3)
            graphic(id = 678)
            animate(id = 4071)
            it.wait(cycles = 2)
            animate(id = -1)
            unlock()
            moveTo(tile = endArea.randomTile)
        }
    }
}
