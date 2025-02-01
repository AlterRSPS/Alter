package org.alter.plugins.content.mechanics.skullremoval

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
import org.alter.game.model.timer.SKULL_ICON_DURATION_TIMER
import org.alter.game.plugin.*

class SkullRemovalPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onTimer(SKULL_ICON_DURATION_TIMER) {
            if (!player.hasSkullIcon(SkullIcon.NONE)) {
                player.setSkullIcon(SkullIcon.NONE)
            }
        }
    }
}
