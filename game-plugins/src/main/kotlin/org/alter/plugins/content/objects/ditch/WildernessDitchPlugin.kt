package org.alter.plugins.content.objects.ditch

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

class WildernessDitchPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onObjOption("object.wilderness_ditch", "cross") {
            val ditch = player.getInteractingGameObj()
            val sideCross = player.tile.z == ditch.tile.z

            val endTile: Tile
            val directionAngle: Int

            if (sideCross) {
                val westOfDitch = player.tile.x < ditch.tile.x

                if (westOfDitch) {
                    endTile = ditch.tile.step(Direction.EAST, 2)
                    directionAngle = Direction.EAST.angle
                } else {
                    endTile = ditch.tile.step(Direction.WEST, 1)
                    directionAngle = Direction.WEST.angle
                }
            } else {
                val southOfDitch = player.tile.z < ditch.tile.z

                if (southOfDitch) {
                    endTile = ditch.tile.step(Direction.NORTH, 2)
                    directionAngle = Direction.NORTH.angle
                } else {
                    endTile = ditch.tile.step(Direction.SOUTH, 1)
                    directionAngle = Direction.SOUTH.angle
                }
            }

            val movement = ForcedMovement.of(player.tile, endTile, clientDuration1 = 33, clientDuration2 = 60, directionAngle = directionAngle)
            player.crossDitch(movement)
        }
    }

    fun Player.crossDitch(movement: ForcedMovement) {
        queue {
            playSound(2452)
            animate(6132)
            forceMove(this, movement)
        }
    }
}
