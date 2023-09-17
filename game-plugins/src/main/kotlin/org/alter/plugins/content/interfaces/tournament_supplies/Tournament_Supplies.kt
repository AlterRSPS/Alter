package org.alter.plugins.content.interfaces.tournament_supplies

import org.alter.game.model.entity.Player
import org.alter.api.InterfaceDestination
import org.alter.api.ext.openInterface
import org.alter.api.ext.runClientScript
import org.alter.api.ext.setInterfaceEvents
import org.alter.api.ext.setInterfaceUnderlay

object Tournament_Supplies {
    var TOURNAMENT_SUPPLIES_INTERFACE = 100
    var TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE = 115

    fun open(p: Player) {
        p.setInterfaceUnderlay(-1, -1)
        p.openInterface(TOURNAMENT_SUPPLIES_INTERFACE, InterfaceDestination.MAIN_SCREEN)
        p.openInterface(TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE, InterfaceDestination.TAB_AREA)
        p.setInterfaceEvents(TOURNAMENT_SUPPLIES_INTERFACE, component = 4, range = 0..525, setting = 1086)
        p.setInterfaceEvents(TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE, component = 0, range = 0..27, setting = 1086)
        p.runClientScript(id = 149, 7536640, 93, 4, 7, 0, -1, "Destroy<col=ff9040>", "Destroy-All<col=ff9040>", "", "", "")
    }
}