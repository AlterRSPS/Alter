package gg.rsmod.plugins.content.inter.tournament_supplies

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.openInterface
import gg.rsmod.plugins.api.ext.runClientScript
import gg.rsmod.plugins.api.ext.setInterfaceEvents
import gg.rsmod.plugins.api.ext.setInterfaceUnderlay

object Tournament_Supplies {
    var TOURNAMENT_SUPPLIES_INTERFACE = 100
    var TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE = 115

    fun open(p: Player) {
        p.setInterfaceUnderlay(-1, -1)
        p.openInterface(TOURNAMENT_SUPPLIES_INTERFACE, InterfaceDestination.MAIN_SCREEN)
        p.openInterface(TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE, InterfaceDestination.TAB_AREA)
        p.setInterfaceEvents(TOURNAMENT_SUPPLIES_INTERFACE, component = 4, range = 0..491, setting = 1086)
        p.setInterfaceEvents(TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE, component = 0, range = 0..27, setting = 1086)
        p.runClientScript(id = 149, 7536640, 93, 4, 7, 0, -1, "Destroy<col=ff9040>", "Destroy-All<col=ff9040>", "", "", "")
    }
}