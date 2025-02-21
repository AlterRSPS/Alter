package org.alter.plugins.content.areas.lumbridge.objs

import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.move.MovementQueue
import org.alter.game.model.move.walkTo
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.rscm.RSCM.getRSCM

class AlkharidGate(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    private val gates = arrayOf("object.gate_44052", "object.gate_44053")
    private val guard = getRSCM("npc.border_guard")

    init {
        gates.forEach { obj ->
            onObjOption(obj, "pay-toll(10gp)", lineOfSightDistance = 2) {
                if (player.inventory.getItemCount(getRSCM("item.coins_995")) >= 10) {
                    when (obj) {
                        "object.gate_44052" -> handleGate(player, world)
                        "object.gate_44053" -> handleGate(player, world)
                    }
                } else {
                    player.queue { this.dialog() }
                }
            }

            onObjOption(obj, "open") {
                player.queue { dialog() }
            }
        }
    }

    private fun handleGate(player: Player, world: World) {
        player.inventory.remove("item.coins_995", 10)

        world.queue {
//            player.lock() // This locking shit needs fixing, no idea how it works tbh
//            wait(2)
            world.openDoor(world.getObject(Tile(3268, 3228), 0)!!, 1574)
            world.openDoor(world.getObject(Tile(3268, 3227), 0)!!, 1573, invertRot = true)

            player.walkTo(3268, player.tile.z, MovementQueue.StepType.FORCED_WALK)
            wait(3)

            world.closeDoor(
                world.getObject(Tile(3267, 3227), 0)!!,
                "object.gate_44052",
                invertTransform = true,
                invertRot = true
            )
            world.closeDoor(world.getObject(Tile(3267, 3228), 0)!!, "object.gate_44053")

//            player.unlock()
        }
    }

    suspend fun QueueTask.dialog() {
        chatPlayer("Can I come through this gate?", animation = 588)
        chatNpc(npc = guard, message = "You must pay a toll of 10 gold coins to pass.", animation = 590)

        when (options("No thank you, I'll walk around.", "Who does my money go to?", "Yes, ok.")) {
            1 -> {
                chatPlayer("No thank you, I'll walk around.", animation = 554)
                chatNpc(npc = guard, message = "Ok suit yourself.", animation = 588)
            }

            2 -> {
                chatPlayer("Who does my money go to?", animation = 554)
                chatNpc(npc = guard, message = "The money goes to the city of Al-Kharid.", animation = 590)
            }

            3 -> {
                chatPlayer("Yes, ok.", animation = 554)

                /** TODO
                 * make different walking from multi tiles
                 * make block for multi accounts
                 * add in after quest dialog & open gate freely
                 * */
            }
        }
    }
}