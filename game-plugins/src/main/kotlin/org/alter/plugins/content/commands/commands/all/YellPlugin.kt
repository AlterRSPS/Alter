package org.alter.plugins.content.commands.commands.all

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
class YellPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("yell", description = "Yell to everyone") {

            val rank: String
            val color: String

            when (player.privilege.id) {
                0 -> {
                    rank = "Player"
                    color = ""
                }
                1 -> {
                    rank = "<img=0>Moderator"
                    color = "<shad=16711680>"
                }
                2 -> {
                    rank = "<img=1>Admin"
                    color = "<shad=65280>"
                }
                3 -> {
                    rank = "<img=21>Developer"
                    color = "<shad=53247>"
                }
                4 -> {
                    rank = "<img=1>Owner"
                    color = "<shad=16777215>"
                }
                5 -> {
                    rank = "<img=8>Donator"
                    color = "<shad=16711680>"
                }

                else -> {
                    rank = "unidentified"
                    color = ""
                }
            }

            val args = player.getCommandArgs()
            val name = player.username
            val text = args[0]
            player.world.players.forEach {
                it.message("$color[$rank]$name:$text", ChatMessageType.ENGINE)
            }
        }
    }
}
