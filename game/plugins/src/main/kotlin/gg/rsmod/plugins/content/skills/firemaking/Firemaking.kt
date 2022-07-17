package gg.rsmod.plugins.content.skills.firemaking

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.filterableMessage
import gg.rsmod.plugins.api.ext.interpolate
import gg.rsmod.plugins.api.ext.player

object Firemaking {


    suspend fun burnLog(it: QueueTask, log: LogData) {
        if(!canBurn(it.player, log)) {
            return
        }
        val player = it.player

        // Drop the logs on the ground
        player.inventory.remove(log.log)
        val logDrop = GroundItem(log.log, 1, player.tile, player)
        player.world.spawn(logDrop)
        player.filterableMessage("You attempt to light the logs.")

        while(true) {
            player.animate(733)
            it.wait(2)

            if(!canBurn(player,log)) {
                player.animate(-1)
                break
            }

            val level = player.getSkills().getCurrentLevel(Skills.FIREMAKING)
            if(level.interpolate(minChance = 60, maxChance = 120, minLvl = 1, maxLvl = 99, cap = 255)) {
                player.addXp(Skills.FIREMAKING, log.xp)

                val world = player.world

                world.queue {
                    val fire = DynamicObject(Objs.FIRE_26185, 10, 0, logDrop.tile)
                    val randBurnTicks = (150..300).random()
                    world.remove(logDrop)
                    world.spawn(fire)
                    wait(randBurnTicks)
                    world.remove(fire)
                    val ashes = GroundItem(Items.ASHES, 1, fire.tile)
                    world.spawn(ashes)

                    // despawn after 2 min
                    wait(200)
                    world.remove(ashes)
                }

                player.animate(-1)

                var targetWalkTile: Tile = Tile(player.tile.x-1, player.tile.z, player.tile.height)
                if(player.world.collision.isBlocked(targetWalkTile, Direction.WEST, false)) {
                    targetWalkTile = Tile(player.tile.x+1, player.tile.z, player.tile.height)
                    if(player.world.collision.isBlocked(targetWalkTile, Direction.EAST, false)) {
                        targetWalkTile = player.tile
                    }
                }

                if(targetWalkTile.getDistance(player.tile) > 0) {
                    player.walkTo(targetWalkTile, MovementQueue.StepType.NORMAL, false)
                }
                break
            }
            it.wait(6)
        }
    }

    private fun canBurn(player: Player, log: LogData): Boolean {
        if(player.getSkills().getCurrentLevel(Skills.FIREMAKING) < log.level) {
            player.filterableMessage("You need a Firemaking level of atleast ${log.level} to light this.")
            return false
        }

        if(!player.inventory.contains(Items.TINDERBOX)) {
            player.filterableMessage("You do not have any fire source to light this.")
            return false
        }

        return true
    }
}