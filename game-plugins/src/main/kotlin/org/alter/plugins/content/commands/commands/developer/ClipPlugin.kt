package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.model.collision.get
import org.alter.game.model.collision.rayCast
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class ClipPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onCommand("clip", Privilege.DEV_POWER) {
            player.message("Tile flags: ${world.collision.get(player.tile)}")
            Direction.RS_ORDER.forEach { dir ->
                val destTile = player.tile.step(dir)
                val walkBlocked = world.lineValidator.rayCast(player.tile, destTile, projectile = false)
                val projectileBlocked = world.lineValidator.rayCast(player.tile, destTile, projectile = true)
                val walkable = if (walkBlocked) "<col=801700>blocked</col>" else "<col=178000>walkable</col>"
                val projectile = if (projectileBlocked) "<col=801700>projectiles blocked" else "<col=178000>projectiles allowed"
                player.message("$dir: $walkable - $projectile")
            }
        }
    }
}