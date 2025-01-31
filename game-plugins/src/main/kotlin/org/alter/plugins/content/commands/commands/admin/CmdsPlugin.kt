package org.alter.plugins.content.commands.commands.admin

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
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class CmdsPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {

        onCommand(command = "cmds", powerRequired = Privilege.ADMIN_POWER, description = "Display commands") {
            player.setInterfaceUnderlay(color = -1, transparency = -1)
            player.openInterface(187, dest = InterfaceDestination.MAIN_SCREEN)
            /**
             * IfOpenSub(id = 187, topInterface = 164, topComponent = 16, modal = true)
             * ClientScript(id = 217, converted = ["Debug", "Teleports|All Gear|Gear Loadouts|New Items|Swap Prayerbook|Swap Spellbook", true], raw = ["Debug", "Teleports|All Gear|Gear Loadouts|New Items|Swap Prayerbook|Swap Spellbook", 1], types = [ss1])
             * IfSetEvents(interfaceId = 187, componentId = 3, startIndex = 0, endIndex = 127, events = Continue)
             */
            TODO("CMDS")
            //player.runClientScript(CommonClientScripts.INTERFACE_MENU, "Command List:", getCommands(getPluginRepository()).joinToString("|").removePrefix("(").removePrefix(")"))
        }

        fun getCommands(r: PluginRepository): List<String> {
            val str_list = ArrayList<String>()
            r.commandPlugins.forEach { (t, _) ->
                var value = "::$t"
                if (r.getDescription(t) != "") {
                    value += " = [ ${r.getDescription(t)} ]"
                }
                str_list.add(value)
            }
            return str_list
        }
    }
}
