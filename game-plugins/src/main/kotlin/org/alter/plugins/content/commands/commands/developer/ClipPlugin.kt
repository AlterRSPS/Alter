package org.alter.plugins.content.commands.commands.developer

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

class ClipPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("clip", Privilege.DEV_POWER) {
            val chunk = world.chunks.getOrCreate(player.tile)
            val matrix = chunk.getMatrix(player.tile.height)
            val lx = player.tile.x % 8
            val lz = player.tile.z % 8
            player.message("Tile flags: ${chunk.getMatrix(player.tile.height).get(lx, lz)}")
            Direction.RS_ORDER.forEach { dir ->
                val walkBlocked = matrix.isBlocked(lx, lz, dir, projectile = false)
                val projectileBlocked = matrix.isBlocked(lx, lz, dir, projectile = true)
                val walkable = if (walkBlocked) "<col=801700>blocked</col>" else "<col=178000>walkable</col>"
                val projectile = if (projectileBlocked) "<col=801700>projectiles blocked" else "<col=178000>projectiles allowed"
                player.message("$dir: $walkable - $projectile")
            }
        }
    }
}
