package org.alter.plugins.content.interfaces.xpdrops

import org.alter.api.CommonClientScripts
import org.alter.api.InterfaceDestination
import org.alter.api.cfg.Varbit
import org.alter.api.ext.*
import org.alter.game.model.entity.Player
import org.alter.game.type.interfacedsl.InterfaceFlag

object XpSettings {
    val SETUP_INTERFACE_ID = 137

    fun open(p: Player) {
        p.runClientScript(CommonClientScripts.MAIN_MODAL_OPEN, -1, -1)
        p.openInterface(dest = InterfaceDestination.MAIN_SCREEN, interfaceId = SETUP_INTERFACE_ID)
        p.setInterfaceEvents(interfaceId = 137, component = 59, 1..2, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 58, 1..3, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 57, 1..2, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 56, 1..8, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 51, 1..3, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 17, 0..24, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 55, 1..32, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 54, 1..32, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 53, 1..4, InterfaceFlag.ClickOp1)
        p.setInterfaceEvents(interfaceId = 137, component = 52, 1..3, InterfaceFlag.ClickOp1)
        if (p.getVarbit(Varbit.EXPERIENCE_TRACKER_CONFIGURED_SKILL) > 0) {
            p.setVarbit(Varbit.EXPERIENCE_TRACKER_CONFIGURED_SKILL, 0)
        }
    }
}
