package org.alter.plugins.content.mechanics.bankpin

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class BankPinPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    val INTERFACE_ID = 14

    init {
        onInterfaceOpen(INTERFACE_ID) {
            open_settings(player)
        }
    }
    



fun open_settings(p: Player) {
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 0, hidden = false)
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 28, hidden = true)
    p.setComponentText(interfaceId = INTERFACE_ID, component = 6, text = "No PIN set")
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 18, hidden = false)
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 21, hidden = true)
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 26, hidden = true)
    p.setComponentText(interfaceId = INTERFACE_ID, component = 8, text = "7 days")
    p.setComponentText(interfaceId = INTERFACE_ID, component = 10, text = "Always lock")
    p.setComponentText(
        interfaceId = INTERFACE_ID,
        component = 14,
        text = "Customers are reminded that they should NEVER tell anyone their Bank PINs or passwords, nor should they ever enter their PINs on any website form.",
    )
}

}
