package org.alter.plugins.content.interfaces.gameframe.xpdrops

import org.alter.plugins.content.interfaces.xpdrops.XpSettings

val INTERFACE_ID = 122

on_button(interfaceId = 160, component = 5) {
    val option = player.getInteractingOption()
    player.playSound(Sound.INTERFACE_SELECT1)

    when (option) {
        1 -> {
            player.toggleVarbit(Varbit.XP_DROPS_VISIBLE_VARBIT)
            if (player.getVarbit(Varbit.XP_DROPS_VISIBLE_VARBIT) == 1) {
                player.openInterface(INTERFACE_ID, InterfaceDestination.XP_COUNTER)
            } else {
                player.closeInterface(INTERFACE_ID)
            }
        }
        2 -> {
            if (player.lock.canInterfaceInteract()) {
                XpSettings.open(player)
            }
        }
    }
}
