package org.alter.plugins.content.commands.commands.all

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.collision.isClipped
import org.alter.game.model.entity.Player
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class ColGridPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onCommand("col_grid") {
            val args = player.getCommandArgs()
            val p = world.getPlayerForName(args[0].replace("_", " ")) ?: return@onCommand
            printGridAroundTile(p, p.getCentreTile())
        }
    }

    fun printGridAroundTile(
        player: Player,
        centerTile: Tile,
    ) {
        val areaSize = 12
        val builder = StringBuilder()
        builder.append("---------------------------------------------------\n")
        // Start from the top (north) and move downwards (south)
        for (y in (centerTile.z + areaSize) downTo (centerTile.z - areaSize)) {
            for (x in (centerTile.x - areaSize)..(centerTile.x + areaSize)) {
                val tile = Tile(x, y)
                if (x == centerTile.x && y == centerTile.z) {
                    builder.append("x")
                } else if (world.collision.isClipped(tile)) {
                    builder.append("+")
                } else {
                    builder.append("-")
                }
            }
            builder.append("\n")
        }
        builder.append("---------------------------------------------------\n")
        World.logger.info { builder.toString() }
    }

}
