package org.alter.plugins.content.interfaces.gameframe.tabs.logout

import net.rsprot.protocol.game.outgoing.logout.Logout
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
import org.alter.game.model.timer.ACTIVE_COMBAT_TIMER
import org.alter.game.plugin.*

/**
 * Logout button.
 */
class LogOutPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onButton(interfaceId = 182, component = 8) {
            if (!player.timers.has(ACTIVE_COMBAT_TIMER)) {
                player.requestLogout()
                player.write(Logout)
                player.session?.requestClose()
                player.channelFlush()
            } else {
                player.message("You can't log out until 10 seconds after the end of combat.")
            }
        }
    }
}
