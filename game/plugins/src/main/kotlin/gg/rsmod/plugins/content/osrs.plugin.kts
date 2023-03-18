package gg.rsmod.plugins.content

import gg.rsmod.game.model.attr.LAST_LOGIN_ATTR
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
    // Skill-related logic.
    player.calculateAndSetCombatLevel()
    if (player.getSkills().getBaseLevel(Skills.HITPOINTS) < 10) {
        player.getSkills().setBaseLevel(Skills.HITPOINTS, 10)
    }
    player.calculateAndSetCombatLevel()
    player.sendWeaponComponentInformation()
    player.sendCombatLevelText()

    /**
     * Still don't know which one of this triggers it, but one of them fixes the chatbox username
     * Ok turns out we send out PlayerUpdate either too late or too soon. Will need to inspect it one day @TODO
     */
    val someRandomVarps = listOf(18,20,21,23,25,46,153,168,169,281,300,849,850,851,852,853,854,855,856,872)
    val someRandomVarbits = listOf(5411,5412,4137,3216,10066,1782,2885,3638,3713,6363,8354,5605,5607,3924,5102,1303,4609,4702)
    someRandomVarbits.forEach {
        player.setVarbit(it, 0)
    }
    someRandomVarps.forEach {
        player.setVarp(it, 0)
    }

    val now = System.currentTimeMillis()
    val last = player.attr.getOrDefault(LAST_LOGIN_ATTR, now.toString()).toLong()
    val time_lapsed = now - last
    player.attr[LAST_LOGIN_ATTR] = now.toString()
    val memberRecurring = 0 // no subs system support as of now
    val noLinkedEmail = 0 // set to 1 disables inbox button??
    player.setInterfaceEvents(interfaceId = 149, component = 0, range = 0 .. 27,
        setting = arrayOf(
            InterfaceEvent.ClickOp2, InterfaceEvent.ClickOp3, InterfaceEvent.ClickOp4, InterfaceEvent.ClickOp6,
            InterfaceEvent.ClickOp7, InterfaceEvent.ClickOp10, InterfaceEvent.UseOnGroundItem, InterfaceEvent.UseOnNpc, InterfaceEvent.UseOnObject,
            InterfaceEvent.UseOnPlayer, InterfaceEvent.UseOnInventory, InterfaceEvent.UseOnComponent, InterfaceEvent.DRAG_DEPTH1, InterfaceEvent.DragTargetable,
            InterfaceEvent.ComponentTargetable
        )
    )
    // Interface-related logic.
    //player.setInterfaceEvents(interfaceId = 149, component = 0, range = 0..27, setting = 3407068)
    player.openDefaultInterfaces()
    // Inform the client whether or not we have a display name.
    val displayName = player.username.isNotBlank()
    //player.runClientScript(1105, if (displayName) 1 else 0) // Has display name
    //player.runClientScript(423, player.username)
    player.setVarbit(13027, player.combatLevel)
    if (player.getVarp(1055) == 0 && displayName) {
        player.syncVarp(1055)
    }
    player.setVarbit(8119, 1) // Has display name

    // Sync attack priority options.
    player.syncVarp(Varps.NPC_ATTACK_PRIORITY_VARP)
    player.syncVarp(Varps.PLAYER_ATTACK_PRIORITY_VARP)

    // Send player interaction options.
    player.sendOption("Follow", 3)
    player.sendOption("Trade with", 4)
    player.sendOption("Report", 5)

    // Game-related logic.
    player.sendRunEnergy(player.runEnergy.toInt())
    player.message("Welcome to ${world.gameContext.name}.", ChatMessageType.GAME_MESSAGE)

    /**
 * @TODO REV 211.... */
//    player.social.pushFriends(player)
//    player.social.pushIgnores(player)
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
            if (pane == InterfaceDestination.XP_COUNTER && player.getVarbit(Varbits.XP_DROPS_VISIBLE_VARBIT) == 0) {
                return@forEach
            } else if (pane == InterfaceDestination.MINI_MAP && player.getVarbit(Varbits.HIDE_DATA_ORBS_VARBIT) == 1) {
                return@forEach
            } else if (pane == InterfaceDestination.QUEST_ROOT) {
                when (player.getVarbit(Varbits.PLAYER_SUMMARY_FOCUS_TAB)) {
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
        player.setVarbit(Varbits.PLAYER_SUMMARY_FOCUS_TAB, 1)
        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 399, 1)
    }

    on_button(InterfaceDestination.QUEST_ROOT.interfaceId,13) {
        player.setVarbit(Varbits.PLAYER_SUMMARY_FOCUS_TAB, 2)
        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 259, 1)
    }

    on_button(InterfaceDestination.QUEST_ROOT.interfaceId,18) {
        player.setVarbit(Varbits.PLAYER_SUMMARY_FOCUS_TAB, 3)
        player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 245, 1)
    }
    on_button(245, 20) {
        player.openInterface(interfaceId = 626, dest = InterfaceDestination.MAIN_SCREEN)
    }

