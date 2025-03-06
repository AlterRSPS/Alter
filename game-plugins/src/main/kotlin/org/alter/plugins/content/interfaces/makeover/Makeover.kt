package org.alter.plugins.content.interfaces.makeover

import dev.openrune.cache.CacheManager
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.api.EquipmentType
import org.alter.api.InterfaceDestination
import org.alter.api.cfg.Enums
import org.alter.api.cfg.Varbit
import org.alter.api.cfg.Varp
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.attr.ORIGINAL_APPEARANCE
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.TaskPriority
import org.alter.game.model.timer.TimerKey
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.type.interfacedsl.InterfaceFlag
import org.alter.rscm.RSCM.asRSCM

class Makeover(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        val slotToSkinColor = listOf(7, 0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12)
        val makeoverEnums = intArrayOf(
            Enums.MAKEOVER_HAIR_LIST,
            Enums.MAKEOVER_BEARD_LIST,
            Enums.MAKEOVER_TORSOS_LIST,
            Enums.MAKEOVER_SLEEVES_LIST,
            Enums.MAKEOVER_BOTTOMS_LIST,
            Enums.MAKEOVER_SHOES_LIST,
            Enums.MAKEOVER_WRISTWEAR
        )
        val makeoverComponentMap: MutableMap<Int, MutableList<Int?>> = makeoverEnums.associateWith {
            MutableList<Int?>(CacheManager.getEnum(it).getSize()) { null }
        }.toMutableMap()
        onWorldInit {
            makeoverEnums.forEach { enum ->
                CacheManager.getEnum(enum).values.forEach { enumEntry ->
                    CacheManager.getDBRow(enumEntry.value as Int).columnValues?.forEachIndexed { index, values ->
                        if (index == 1) {
                            values?.forEachIndexed { _, v ->
                                if (v is Int) {
                                    makeoverComponentMap[enum]?.set(enumEntry.key, v)
                                } else {
                                    logger.error { "Makeover property mismatch expected Int : ${enumEntry.key} : ${enumEntry.value}" }
                                }
                            }
                        }
                    }
                }
            }
        }
        val makeover_cover = TimerKey()
        onTimer(makeover_cover) {
            player.graphic(2372)
            player.timers[makeover_cover] = 1
        }
        onButton(makeoverInterface, 7) {
            val slot = player.getInteractingSlot() / 5
            player.setVarp(Varp.MAKEOVER_SLOT, slot)
            val enumSelectionRow = when (player.getVarbit(Varbit.MAKEOVER_INTERFACE_VIEW)) {
                1 -> Enums.MAKEOVER_BEARD_LIST
                3 -> Enums.MAKEOVER_TORSOS_LIST
                4 -> Enums.MAKEOVER_SLEEVES_LIST
                5 -> Enums.MAKEOVER_BOTTOMS_LIST
                6 -> Enums.MAKEOVER_SHOES_LIST
                7 -> Enums.MAKEOVER_WRISTWEAR
                else -> Enums.MAKEOVER_HAIR_LIST
            }
            val kitSlot = when (player.getVarbit(Varbit.MAKEOVER_INTERFACE_VIEW)) {
                1 -> 1  //|  Beard |
                3 -> 2  //|  Body  |
                4 -> 3  //|  3 |  Arms  |
                7 -> 4 //|  4 | Gloves |
                5 -> 5 //|  5 |  Legs  |
                6 -> 6 //|  6 |  Boots |
                else -> 0
            }
            val identKit = makeoverComponentMap[enumSelectionRow]?.get(slot)
            if (identKit != null) {
                player.appearance.setIdentKit(kitSlot, identKit)
            }
        }
        onButton(makeoverInterface, 24) {
            val slot = player.getInteractingSlot() / 2
            player.setVarp(3789, slot)
            val colorMap = when (player.getVarbit(Varbit.MAKEOVER_INTERFACE_VIEW)) {
                3, 4 -> 1 // Sleeves , Torsos
                5 -> 2 // Bottoms
                6 -> 3 // Boats
                else -> 0 // Hair, Facial Hair
            }
            (player).appearance.setColour(colorMap, slot)
        }
        listOf(Pair(makeoverInterface, 31), Pair(bodyTypeInterface, 32)).forEach {
            onButton(it.first, it.second) {
                player.setVarbit(
                    Varbit.MAKEOVER_INTERFACE_VIEW, when (player.getInteractingSlot()) {
                        12 -> 1 // Facial Hair
                        14 -> 2 // Interface [Body Type]
                        16 -> 3 // Torsos
                        18 -> 4 // Sleeves
                        20 -> 5 // Bottoms
                        22 -> 6 // Shoes
                        24 -> 7 // Wristwear
                        else -> 0 // Hair
                    }
                )
                player.closeInterface(makeoverInterface)
                open(player)
            }
        }
        intArrayOf(17,18).forEach { buttonID ->
            onButton(bodyTypeInterface, buttonID) { // If 18 => Close interface
                player.appearance.bodyType = player.getVarp(261)
                player.appearance.setColour(4,player.getVarp(262))
                player.appearance.pronoun = player.getVarp(263)
                player.syncAppearance()
                player.attr[ORIGINAL_APPEARANCE] = player.appearance
                if (buttonID == 18) {
                    player.closeInterface(bodyTypeInterface)
                }
                if (buttonID == 17) {
                    for (i in 0 until 12) {
                        // @TODO
                        // player.appearance.setWornObj(i, -1, -1, -1)
                    }
                }
            }
        }
        intArrayOf(28,27).forEach { buttonID ->
            onButton(makeoverInterface, buttonID) {
                player.attr[ORIGINAL_APPEARANCE] = player.appearance
                player.message("Changes saved?>")
                if (buttonID == 28) {
                    player.closeInterface(makeoverInterface)
                }
            }
        }
        intArrayOf(makeoverInterface, bodyTypeInterface).forEach { inter ->
            onInterfaceClose(inter) {
                //val oldAppearance = player.attr.get(ORIGINAL_APPEARANCE)
                //if (oldAppearance != null) {
                //    player.appearance = oldAppearance
                //    PlayerInfo(player).syncAppearance()
                //}
                player.timers.remove(makeover_cover)
            }
            onInterfaceOpen(inter) {
                player.timers[makeover_cover] = 1
            }
        }
        onButton(bodyTypeInterface, 13) {
            player.setVarp(262, slotToSkinColor.getOrNull(player.getInteractingSlot()) ?: error("[Makeover] Invalid skin slot"))
        }

        onButton(bodyTypeInterface, 21) {
            player.setVarp(263, 0)
        }
        onButton(bodyTypeInterface, 24) {
            player.setVarp(263, 2)
        }
        onButton(bodyTypeInterface, 27) {
            player.setVarp(263, 1)
        }

        onButton(bodyTypeInterface, 8) {
            player.setVarp(263,1)
            player.setVarp(261, 1)
        }
        onButton(bodyTypeInterface, 2) {
            player.setVarp(263,0)
            player.setVarp(261, 0)
        }
        onCommand("makeover") {
            open(player)
        }




    }

    companion object {
        val makeoverInterface = 516
        val bodyTypeInterface = 205
        fun open(player: Player) {
            val headEquipment = player.getEquipment(EquipmentType.HEAD)
            if (headEquipment != null) {
                player.queue(TaskPriority.WEAK) {
                    this.itemMessageBox(
                        player,
                        message = "You should take off your hat if you want to get a<br>haircut!",
                        item = headEquipment.id.asRSCM("item"),
                        amountOrZoom = 400
                    )
                }
                return
            }
            // @TODO Add GraphicTile: 2372, while player is within MakeOver interface
            player.setInterfaceUnderlay(-1, -2)
            player.attr[ORIGINAL_APPEARANCE] = player.appearance // @TODO AHH shit this initializes it and it sets to default values we need to first implement this shit to appear when user first start. Tmrw we clean this up and then we can also setup starter plugin.
            for (i in 0 until 12) {
            // @TODO
            //player).info.setWornObj(i, -1, -1, -1)
            }
            if (player.getVarbit(Varbit.MAKEOVER_INTERFACE_VIEW) == 2) {
                player.openInterface(bodyTypeInterface, InterfaceDestination.MAIN_SCREEN)
                player.setComponentText(bodyTypeInterface, 19, "Confirm")
                player.setComponentText(bodyTypeInterface, 33, "Apply")
                player.setInterfaceEvents(bodyTypeInterface, 13, 0..12, setting = InterfaceFlag.ClickOp1)
                player.setInterfaceEvents(bodyTypeInterface, 32, 10..24, setting = InterfaceFlag.ClickOp1)
                return
            }
            player.openInterface(makeoverInterface, InterfaceDestination.MAIN_SCREEN)
            var component7Range: IntRange?
            var component24Range: IntRange? = null
            var component27Range: IntRange?
            var component28Range: IntRange?
            var component31Range: IntRange?
            when (player.getVarbit(Varbit.MAKEOVER_INTERFACE_VIEW)) {
                1 -> {
                    component7Range = 0..75
                    component24Range = 0..60
                    component27Range = 0..10
                    component28Range = 0..10
                    component31Range = 10..24
                }

                3 -> {
                    component7Range = 0..100
                    component24Range = 0..58
                    component27Range = 0..10
                    component28Range = 0..10
                    component31Range = 10..24
                }

                4 -> {
                    component7Range = 0..85
                    component24Range = 0..58
                    component27Range = 0..10
                    component28Range = 0..10
                    component31Range = 10..24
                }

                5 -> {
                    component7Range = 0..110
                    component24Range = 0..58
                    component27Range = 0..10
                    component28Range = 0..10
                    component31Range = 10..24
                }

                6 -> {
                    component7Range = 0..10
                    component24Range = 0..12
                    component27Range = 0..10
                    component28Range = 0..10
                    component31Range = 10..24
                }

                7 -> {
                    component7Range = 0..10
                    component27Range = 0..10
                    component28Range = 0..10
                    component31Range = 10..24
                }

                else -> {
                    component7Range = 0..285
                    component24Range = 0..60
                    component27Range = 0..10
                    component28Range = 0..10
                    component31Range = 10..24
                }
            }
            player.setInterfaceEvents(makeoverInterface, 7, component7Range, setting = InterfaceFlag.ClickOp1)
            if (component24Range != null) {
                player.setInterfaceEvents(
                    makeoverInterface,
                    24,
                    component24Range,
                    setting = InterfaceFlag.ClickOp1
                )
            }
            player.setInterfaceEvents(makeoverInterface, 27, component27Range, setting = InterfaceFlag.ClickOp1)
            player.setInterfaceEvents(makeoverInterface, 28, component28Range, setting = InterfaceFlag.ClickOp1)
            player.setInterfaceEvents(makeoverInterface, 31, component31Range, setting = InterfaceFlag.ClickOp1)
        }
        private val logger = KotlinLogging.logger {}
    }
}