package gg.rsmod.plugins.content.items.others.adminitems

import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula

//import gg.rsmod.plugins.content.inter.tournament_supplies.Tournament_Supplies

/**
 * Good old Rotten Potato
 * @author Cl0udS3c
 * @since 2021.09.01
 *
 */
var type = 0
var nr: Int = 0
/**
 * Select number -> Or input w.e you will call it gives wrong Title
 */

/**
 * IDEAS:
 * Add incremental varbit option
 * Paged options
 */
on_item_option(Items.ROTTEN_POTATO, 1){
    // Where do you want to start
    when (type) {
        1 -> {
            player.graphic(-1)
            player.graphic(nr, 100)
            player.message("Playing $nr gfx")
            nr += 1
        }
        2 -> {
            player.animate(-1)
            player.animate(nr)
            player.message("Playing $nr animation")
            nr += 1
        }
        3 -> {
            val p_x = player.tile.x-3
            val p_z = player.tile.z-3
            for (e in p_x..p_x+5) {
                for (i in p_z..p_z+5) {
                    world.spawn(TileGraphic(tile = Tile(x = e, z = i, height = 0), id = nr, height = 30, delay = 3))
                }
            }
            player.message("Spawned 5x5 $nr Gfx ")
            nr += 1
        }
        4 -> {
            player.openInterface(nr, InterfaceDestination.MAIN_SCREEN)
            player.message("Opened $nr interface")
            nr+1
        }
        5 -> {
            val p_x = player.tile.x-3
            val p_z = player.tile.z-3

            for (e in p_x..p_x+10) {
                for (i in p_z..p_z+10) {
                    world.spawn(GroundItem(Item(nr, 1), Tile(e, i, player.tile.height), player))
                    nr += 1
                }
            }
            player.message("Spawned 10x10 from $nr to ${nr+100} items. ")
        }
        6 -> {
            player.playJingle(nr)
            player.message("Playing jingle song: $nr")
            nr+=1
        }
        11 -> {
            player.message("Interacting with 11")
        }
        13 -> {
            player.message("Interacting with 13")
        }
        else -> player.message("You haven't select category, to select the category click on <col=ffff66>Slice</col> option.")
    }
}

on_item_option(Items.ROTTEN_POTATO, 2) {
    player.queue {
        type = selectCategory()
    }
}

on_item_option(Items.ROTTEN_POTATO, 3) {
    player.queue(TaskPriority.STRONG) {
        nr = getStart() ?: return@queue
        player.message("You have set $nr as starting number")
    }
}
on_command("maxcapes") {
    val capes = listOf(Items.MAX_CAPE_13342,
        Items.ATTACK_CAPE, Items.ATTACK_CAPET,
        Items.STRENGTH_CAPE, Items.STRENGTH_CAPET,
        Items.DEFENCE_CAPE, Items.DEFENCE_CAPET,
        Items.RANGING_CAPE, Items.RANGING_CAPET,
        Items.PRAYER_CAPE, Items.PRAYER_CAPET,
        Items.MAGIC_CAPE, Items.MAGIC_CAPET,
        Items.RUNECRAFT_CAPE, Items.RUNECRAFT_CAPET,
        Items.HITPOINTS_CAPE, Items.HITPOINTS_CAPET,
        Items.AGILITY_CAPE, Items.AGILITY_CAPET,
        Items.HERBLORE_CAPE, Items.HERBLORE_CAPET,
        Items.THIEVING_CAPE, Items.THIEVING_CAPET,
        Items.CRAFTING_CAPE, Items.CRAFTING_CAPET,
        Items.FLETCHING_CAPE, Items.FLETCHING_CAPET,
        Items.SLAYER_CAPE, Items.SLAYER_CAPET,
        Items.CONSTRUCT_CAPE, Items.CONSTRUCT_CAPET,
        Items.MINING_CAPE, Items.MINING_CAPET,
        Items.SMITHING_CAPE, Items.SMITHING_CAPET,
        Items.FISHING_CAPE, Items.FISHING_CAPET,
        Items.COOKING_CAPE, Items.COOKING_CAPET,
        Items.FIREMAKING_CAPE, Items.FIREMAKING_CAPET,
        Items.WOODCUTTING_CAPE, Items.WOODCUT_CAPET,
        Items.FARMING_CAPE, Items.FARMING_CAPET,
        Items.HUNTER_CAPE, Items.HUNTER_CAPET,
        Items.CABBAGE_CAPE,
        Items.QUEST_POINT_CAPE, Items.QUEST_POINT_CAPE_T,
        Items.ACHIEVEMENT_DIARY_CAPE, Items.ACHIEVEMENT_DIARY_CAPE_T,
        Items.MUSIC_CAPE, Items.MUSIC_CAPET)

    for (cape in capes) {
        player.bank.add(cape, 2)
    }
}
suspend fun QueueTask.getStart(): Int {
    return inputInt("From what ID do you want to start:")
}
suspend fun QueueTask.selectCategory(): Int {
    val type = when (options(
        "Animations", // On clicking Option 1 it will play Animation (On the player) increase [nr]
        "Graphics (GFX)", // On clicking Option it will play TileGraphic's + Each time increase [nr]
        "Graphics (GFX) Area 5x5", // On click will play TileGraphic's that is set on nr in 5x5 area + after Graphics finish playing the NR increases
        "Interface", // Open's Interface + each click will open and increast [nr]
        "Item Drop 10x10", // Drops random item starting from NR and increase on each Tile
        "Jingle", // Drops random item starting from NR and increase on each Tile
        "Jingle2", // Drops random item starting from NR and increase on each Tile
        "Jingle3", // Drops random item starting from NR and increase on each Tile
        "Jingle4", // Drops random item starting from NR and increase on each Tile
        "Jingle5", // Drops random item starting from NR and increase on each Tile
        "Jingle6", // Drops random item starting from NR and increase on each Tile
        "Jingle7", // Drops random item starting from NR and increase on each Tile
        "Jingle8", // Drops random item starting from NR and increase on each Tile
        title = "Type to increase each time you click on Eat?")) {
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> 4
        5 -> 5
        6 -> 6
        7 -> 7
        8 -> 8
        9 -> 9
        11 -> 11
        12 -> 12
        13 -> 13
        else -> return 0
    }
    nr = 0
    return type
}
