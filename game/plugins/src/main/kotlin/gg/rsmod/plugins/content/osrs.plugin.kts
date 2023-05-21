package gg.rsmod.plugins.content

import gg.rsmod.plugins.content.Osrs_plugin.OSRSInterfaces.openDefaultInterfaces

/**
 * Closing main modal for players.
 */
set_modal_close_logic {
    val modal = player.interfaces.getModal()
    if (modal != -1) {
        player.closeInterface(modal)
        player.interfaces.setModal(-1)
    }
}

/**
 * Check if the player has a menu opened.
 */
set_menu_open_check {
    player.getInterfaceAt(dest = InterfaceDestination.MAIN_SCREEN) != -1
}

/**
 * Execute when a player logs in.
 */
on_login {
    with (player) {
        // Skill-related logic.
        calculateAndSetCombatLevel()
        if (getSkills().getBaseLevel(Skills.HITPOINTS) < 10) {
            getSkills().setBaseLevel(Skills.HITPOINTS, 10)
        }
        calculateAndSetCombatLevel()
        sendWeaponComponentInformation()
        sendCombatLevelText()
        setInterfaceEvents(interfaceId = 149, component = 0, range = 0 .. 27,
            setting = arrayOf(
                InterfaceEvent.ClickOp2, InterfaceEvent.ClickOp3, InterfaceEvent.ClickOp4, InterfaceEvent.ClickOp6,
                InterfaceEvent.ClickOp7, InterfaceEvent.ClickOp10, InterfaceEvent.UseOnGroundItem, InterfaceEvent.UseOnNpc, InterfaceEvent.UseOnObject,
                InterfaceEvent.UseOnPlayer, InterfaceEvent.UseOnInventory, InterfaceEvent.UseOnComponent, InterfaceEvent.DRAG_DEPTH1, InterfaceEvent.DragTargetable,
                InterfaceEvent.ComponentTargetable
            )
        )
        openDefaultInterfaces()
        setVarbit(Varbit.COMBAT_LEVEL_VARBIT, combatLevel)
        setVarbit(Varbit.CHATBOX_UNLOCKED, 1)
        runClientScript(5840)
        if (getVarp(Varp.PLAYER_HAS_DISPLAY_NAME) == 0 && username.isNotBlank()) {
            syncVarp(Varp.PLAYER_HAS_DISPLAY_NAME)
        }
        // Sync attack priority options.
        syncVarp(Varp.NPC_ATTACK_PRIORITY_VARP)
        syncVarp(Varp.PLAYER_ATTACK_PRIORITY_VARP)
        // Send player interaction options.
        sendOption("Follow", 3)
        sendOption("Trade with", 4)
        sendOption("Report", 5)
        // Game-related logic.
        sendRunEnergy(player.runEnergy.toInt())
        message("Welcome to ${world.gameContext.name}.", ChatMessageType.GAME_MESSAGE)
        player.social.pushFriends(player)
        player.social.pushIgnores(player)
        }
    }



object OSRSInterfaces {
    fun Player.openDefaultInterfaces() {
        openOverlayInterface(interfaces.displayMode)
        openModals(this)
        setInterfaceEvents(interfaceId = 239, component = 3, range = 0..665, setting = 6) // enable music buttons
        initInterfaces(interfaces.displayMode)
    }

    fun openModals(player: Player, fullscreen: Boolean = false) {
        InterfaceDestination.getModals().forEach { pane ->
            if (pane == InterfaceDestination.XP_COUNTER && player.getVarbit(Varbit.XP_DROPS_VISIBLE_VARBIT) == 0) {
                return@forEach
            } else if (pane == InterfaceDestination.MINI_MAP && player.getVarbit(Varbit.HIDE_DATA_ORBS_VARBIT) == 1) {
                return@forEach
            } else if (pane == InterfaceDestination.QUEST_ROOT) {
                when (player.getVarbit(Varbit.PLAYER_SUMMARY_FOCUS_TAB)) {
                    0 -> {
                        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 712, 1)
                    }
                    1 -> {
                        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 399, 1)
                    }
                    2 -> {
                        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 259, 1)
                    }
                    3 -> {
                        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 245, 1)
                    }
                }


                player.setVarbit(11877, 155) // Quests Completed
                player.setVarp(2944, 1404) // Quests Completed
                player.openInterface(pane.interfaceId, pane, fullscreen)
                //player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 712, 1) // quest sub interfaces
                player.setInterfaceEvents(interfaceId = 399, component = 6, range = 0..20, setting = 14)
                player.setInterfaceEvents(interfaceId = 399, component = 7, range = 0..125, setting = 14)
                player.setInterfaceEvents(interfaceId = 399, component = 8, range = 0..13, setting = 14)
                player.setInterfaceEvents(399, 3, 0..7, 14)
                player.setComponentText(399, 9, "Completed: 0/154")
                player.setComponentText(399, 10, "Quest Points: 0/290")
                player.setComponentText(712, 1, text = player.username)
                player.setComponentText(712, 3, text = player.combatLevel.toString())
                return@forEach
            }
            player.openInterface(pane.interfaceId, pane, fullscreen)
        }
    }
}

// Character summary
on_button(InterfaceDestination.QUEST_ROOT.interfaceId, 3) {
    player.setVarbit(8168, 0)
    player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 712, 1)
}

    // Quest Tab
    on_button(InterfaceDestination.QUEST_ROOT.interfaceId,8) {
        player.setVarbit(Varbit.PLAYER_SUMMARY_FOCUS_TAB, 1)
        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 399, 1)
    }

    on_button(InterfaceDestination.QUEST_ROOT.interfaceId,13) {
        player.setVarbit(Varbit.PLAYER_SUMMARY_FOCUS_TAB, 2)
        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 259, 1)
    }

    on_button(InterfaceDestination.QUEST_ROOT.interfaceId,18) {
        player.setVarbit(Varbit.PLAYER_SUMMARY_FOCUS_TAB, 3)
        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 245, 1)
    }
    on_button(245, 20) {
        player.openInterface(interfaceId = 626, dest = InterfaceDestination.MAIN_SCREEN)
    }

