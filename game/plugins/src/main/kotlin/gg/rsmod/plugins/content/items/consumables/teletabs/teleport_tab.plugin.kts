package gg.rsmod.plugins.content.items.consumables.teletabs

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport
import gg.rsmod.plugins.content.magic.prepareForTeleport

private val LOCATIONS = mapOf(
        Items.VARROCK_TELEPORT to Area(3210, 3423, 3216, 3425),
        Items.FALADOR_TELEPORT to Area(2961, 3376, 2969, 3385),
        Items.LUMBRIDGE_TELEPORT to Area(3221, 3218, 3222, 3219),
        Items.CAMELOT_TELEPORT to Area(2756, 3476, 2758, 3480),
        Items.ARDOUGNE_TELEPORT to Area(2659, 3300, 2665, 3310),
        Items.WATCHTOWER_TELEPORT to Area(2551, 3113, 2553, 3116),
        Items.RIMMINGTON_TELEPORT to Area(2953, 3222, 2956, 3226),
        Items.TAVERLEY_TELEPORT to Area(2893, 3463, 2894, 3467),
        Items.POLLNIVNEACH_TELEPORT to Area(3338, 3003, 3342, 3004),
        Items.HOSIDIUS_TELEPORT to Area(1742, 3515, 1743, 3518),
        Items.RELLEKKA_TELEPORT to Area(2668, 3631, 2671, 3632),
        Items.BRIMHAVEN_TELEPORT to Area(2757, 3176, 2758, 3179),
        Items.YANILLE_TELEPORT to Area(2542, 3095, 2545, 3096),
        Items.TROLLHEIM_TELEPORT to Area(2888, 3678, 2893, 3681),
        Items.CATHERBY_TELEPORT to Area(2800, 3449, 2801, 3450),
        Items.BARBARIAN_TELEPORT to Area(2543, 3570, 2544, 3571),
        Items.LUMBRIDGE_GRAVEYARD_TELEPORT to Area(1632, 3839, 1633, 3840),
        Items.DRAYNOR_MANOR_TELEPORT to Area(3108, 3352, 3108, 3352),
        //Items.FELDIP_HILLS_TELEPORT to Area(2542, 2925, 2542, 2925), -> Teleport
        Items.FISHING_GUILD_TELEPORT to Area(2612, 3391, 2612, 3391),
        Items.KHAZARD_TELEPORT to Area(2637, 3166, 2637, 3166),
        Items.MIND_ALTAR_TELEPORT to Area(2979, 3509, 2979, 3509),
        //Items.LU
    // @TODO Items.APE_ATOLL_TELEPORT , Need to have Monkey Madness and Receive 10th Squad Training from Daero
)
//Items.DIGSITE_TELEPORT to Tile(3324, 3411)
//Items.LUMBERYARD_TELEPORT to Tile(3302, 3488)
LOCATIONS.forEach { item, endTile ->
    on_item_option(item = item, option = "break") {
        player.queue(TaskPriority.STRONG) {
            player.teleport(this, endTile, item)
        }
    }
}

suspend fun Player.teleport(it : QueueTask, endArea : Area, tab : Int) {
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