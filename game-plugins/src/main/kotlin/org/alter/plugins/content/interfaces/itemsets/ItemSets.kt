package org.alter.plugins.content.interfaces.itemsets

import org.alter.api.InterfaceDestination
import org.alter.api.ext.InterfaceEvent
import org.alter.api.ext.openInterface
import org.alter.api.ext.setInterfaceEvents
import org.alter.api.ext.setInterfaceUnderlay
import org.alter.game.model.entity.Player

object ItemSets {
    var ITEMSETS_INTERFACE = 451
    var ITEMSETS_INVENTORY = 430

    fun open(p: Player) {
        p.setInterfaceUnderlay(-1, -1)
        p.openInterface(ITEMSETS_INTERFACE, InterfaceDestination.MAIN_SCREEN)
        p.openInterface(ITEMSETS_INVENTORY, InterfaceDestination.TAB_AREA)

        p.setInterfaceEvents(
            ITEMSETS_INTERFACE,
            component = 2,
            range = 0..109,
            setting = arrayOf(InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp10),
        )
        p.setInterfaceEvents(
            ITEMSETS_INVENTORY,
            component = 0,
            range = 0..27,
            setting = arrayOf(InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp10),
        )
    }
}
