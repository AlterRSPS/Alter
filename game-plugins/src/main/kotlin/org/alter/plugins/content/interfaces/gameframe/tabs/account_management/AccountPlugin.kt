package org.alter.plugins.content.interfaces.gameframe.tabs.account_management

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

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
class AccountPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        val BUTTON_ID = 109

        val NAME_CHANGER_ID = 45

        /**
         * Account Tab Buttons
         */
        val buttons: Array<Pair<String, Int>> =
            arrayOf(
                Pair("UPGRADE_NOW_ID", 26),
                Pair("BENEFITS_ID", 31),
                Pair("INBOX_ID", 38),
                Pair("HISTORY_ID", 61),
                Pair("NEWS_ID", 68),
                Pair("ARCHIVE_ID", 73),
                Pair("WEBSITE_ID", 81),
                Pair("GE_ID", 83),
                Pair("WIKI_ID", 85),
                Pair("SUPPORT_ID", 82),
                Pair("HISCORE_ID", 84),
                Pair("MERCH_ID", 86),
            )

        onButton(BUTTON_ID, NAME_CHANGER_ID) {
            player.openInterface(interfaceId = 589, dest = InterfaceDestination.TAB_AREA)
            player.setComponentText(interfaceId = 589, component = 6, text = "Next free change:")
            player.setComponentText(interfaceId = 589, component = 7, text = "Now!") // Make this a method to pull last updated date from your database, return that date, or "Now!"
            player.setInterfaceEvents(interfaceId = 589, component = 18, range = 0..9, setting = 0)
            player.setVarbit(5605, 1)
        }

        buttons.forEach {
            onButton(BUTTON_ID, it.second) {
                player.message("Button: [${it.first} : ${it.second}]")
            }
        }

        listOf(6, 11, 16).forEachIndexed { index, it ->
            onButton(109, it) {
                player.setVarbit(10060, index)
            }
        }
    }
}
