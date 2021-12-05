package gg.rsmod.plugins.content

import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.game.model.attr.LAST_LOGIN_ATTR
import gg.rsmod.game.model.timer.TimeConstants
import gg.rsmod.plugins.content.Osrs_plugin.OSRSInterfaces.openDefaultInterfaces
import gg.rsmod.plugins.content.inter.welcome.WelcomeScreen.openWelcomeScreen

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

    val now = System.currentTimeMillis()
    val last = player.attr.getOrDefault(LAST_LOGIN_ATTR, now.toString()).toLong()
    val time_lapsed = now - last
    player.attr[LAST_LOGIN_ATTR] = now.toString()

    val memberRecurring = 0 // no subs system support as of now
    val noLinkedEmail = 0 // set to 1 disables inbox button??

    player.runClientScript(2498, if(player.hasMembers()) 1 else 0, memberRecurring, noLinkedEmail)

    // Interface-related logic.
    //openWelcomeScreen(player, (time_lapsed/TimeConstants.MINUTE).toInt(), player.membersDaysLeft())

    player.openDefaultInterfaces()
    // Inform the client whether or not we have a display name.
    val displayName = player.username.isNotBlank()
    player.runClientScript(1105, if (displayName) 1 else 0) // Has display name
    player.runClientScript(423, player.username)
    player.setVarbit(13027, player.combatLevel)
    //player.runClientScript(420)
    if (player.getVarp(1055) == 0 && displayName) {
        player.syncVarp(1055)
    }
    player.setVarbit(8119, 1) // Has display name

    // Sync attack priority options.
    player.syncVarp(OSRSGameframe.NPC_ATTACK_PRIORITY_VARP)
    player.syncVarp(OSRSGameframe.PLAYER_ATTACK_PRIORITY_VARP)

    // Send player interaction options.
    player.sendOption("Follow", 3)
    player.sendOption("Trade with", 4)
    player.sendOption("Report", 5)

    // Game-related logic.
    player.sendRunEnergy(player.runEnergy.toInt())
    player.message("Welcome ${player.username} to ${world.gameContext.name}.", ChatMessageType.BROADCAST)
}

/**
 * Logic for swapping items in inventory.
 */
on_component_item_swap(interfaceId = 149, component = 0) {
    val srcSlot = player.attr[INTERACTING_ITEM_SLOT]!!
    val dstSlot = player.attr[OTHER_ITEM_SLOT_ATTR]!!

    val container = player.inventory

    if (srcSlot in 0 until container.capacity && dstSlot in 0 until container.capacity) {
        container.swap(srcSlot, dstSlot)
    } else {
        // Sync the container on the client
        container.dirty = true
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
            if (pane == InterfaceDestination.XP_COUNTER && player.getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 0) {
                return@forEach
            } else if (pane == InterfaceDestination.MINI_MAP && player.getVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT) == 1) {
                return@forEach
            } else if (pane == InterfaceDestination.QUEST_ROOT) {
                player.openInterface(pane.interfaceId, pane, fullscreen)
                player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 399, 1) // quest sub interfaces
                player.setInterfaceEvents(interfaceId = 399, component = 6, range = 0..20, setting = 14)
                player.setInterfaceEvents(interfaceId = 399, component = 7, range = 0..125, setting = 14)
                player.setInterfaceEvents(interfaceId = 399, component = 8, range = 0..13, setting = 14)
                return@forEach
            }
            player.openInterface(pane.interfaceId, pane, fullscreen)
        }
    }
}