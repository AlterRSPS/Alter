package gg.rsmod.plugins.content.interfaces.itemsets

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.InterfaceEvent
import gg.rsmod.plugins.api.ext.openInterface
import gg.rsmod.plugins.api.ext.setInterfaceEvents
import gg.rsmod.plugins.api.ext.setInterfaceUnderlay

object ItemSets {
    var ITEMSETS_INTERFACE = 451
    var ITEMSETS_INVENTORY = 430

    fun open(p: Player) {
        p.setInterfaceUnderlay(-1, -1)
        p.openInterface(ITEMSETS_INTERFACE, InterfaceDestination.MAIN_SCREEN)
        p.openInterface(ITEMSETS_INVENTORY, InterfaceDestination.TAB_AREA)

        p.setInterfaceEvents(ITEMSETS_INTERFACE, component = 2, range = 0..105, setting = arrayOf(InterfaceEvent.ClickOp1,InterfaceEvent.ClickOp10))
        p.setInterfaceEvents(ITEMSETS_INVENTORY, component = 0, range = 0..27, setting = arrayOf(InterfaceEvent.ClickOp1,InterfaceEvent.ClickOp10))
    }
}