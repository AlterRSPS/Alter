package org.alter.plugins.content.mechanics.appearance

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

class LoginAppearancePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onLogin {
//    if (player.attr[APPEARANCE_SET_ATTR] == false) {
//        player.queue(TaskPriority.STRONG) {
//            player.lock = LockState.FULL_WITH_LOGOUT
//            selectAppearance()
//        }
//    }
        }
    }
}
