package org.alter.plugins.content.interfaces.gameframe.tabs.prayer

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
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */
class PrayerbookPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("prayerbook") {
            // player.setVarbit(14826) // 0 = For normal prayer book , 1 = for the new book
            // ClientScript(id = 2158)
            // ClientScript(id = 915, converted = [5], raw = [5], types = [i])
            // IfCloseSub(topInterface = 164, topComponent = 16)
            // IfSetEvents(interfaceId = 541, componentId = 41, startIndex = 0, endIndex = 4, events = ClickOp1)
        }
    }
}
