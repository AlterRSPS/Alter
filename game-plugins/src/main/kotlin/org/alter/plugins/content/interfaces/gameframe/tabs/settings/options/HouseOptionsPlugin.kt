package org.alter.plugins.content.interfaces.gameframe.tabs.settings.options

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

class HouseOptionsPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        
    }
    
// on_button(interfaceId = OPTIONS_INTERFACE_ID, component = HOUSE_OPT_BUTTON_ID) {
//    /**
//     Teleport inside + doors: varp 1047
//     */
//    if (!player.lock.canInterfaceInteract()) {
//        return@on_button
//    }
//    player.openInterface(interfaceId = HOUSE_OPT_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)
//    player.setComponentText(interfaceId = HOUSE_OPT_INTERFACE_ID, component = 20, text = "Number of rooms: 9")
// }
//
// /**
// * Close.
// */
// on_button(interfaceId = HOUSE_OPT_INTERFACE_ID, component = 21) {
//    player.closeInterface(interfaceId = HOUSE_OPT_INTERFACE_ID)
// }

}
