package org.alter.game.model.move

import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.model.attr.*
import org.alter.game.model.entity.*
import org.alter.game.model.queue.TaskPriority
import org.alter.game.plugin.Plugin

object PawnPathAction {
    private const val ITEM_USE_OPCODE = -1

    val walkPlugin: Plugin.() -> Unit = {
        val pawn = ctx as Pawn
        val world = pawn.world
        val other = pawn.attr[INTERACTING_NPC_ATTR]?.get() ?: pawn.attr[INTERACTING_PLAYER_ATTR]?.get()!!
        val opt = pawn.attr[INTERACTING_OPT_ATTR]!!

        /*
         * Some interactions only require line-of-sight range, such as npcs
         * behind cells or booths. This allows for diagonal interaction.
         *
         * Set to null for default interaction range.
         */
        val lineOfSightRange = if (other is Npc) world.plugins.getNpcInteractionDistance(other.id) else null
        pawn.queue(TaskPriority.STANDARD) {
            terminateAction = {
                TODO("pawn.stopMovement()")
                if (pawn is Player) {
                    pawn.write(SetMapFlag(255, 255))
                }
            }
            TODO("walk(this, pawn, other, opt, lineOfSightRange) || Wip ")
        }
    }

    val itemUsePlugin: Plugin.() -> Unit = s@{
        val pawn = ctx as Pawn
        val world = pawn.world
        val other = pawn.attr[INTERACTING_NPC_ATTR]?.get() ?: pawn.attr[INTERACTING_PLAYER_ATTR]?.get()!!
        /*
         * Some interactions only require line-of-sight range, such as npcs
         * behind cells or booths. This allows for diagonal interaction.
         *
         * Set to null for default interaction range.
         */
        val lineOfSightRange = if (other is Npc) world.plugins.getNpcInteractionDistance(other.id) else null
        pawn.queue(TaskPriority.STANDARD) {
            terminateAction = {
                TODO("pawn.stopMovement()")
                if (pawn is Player) {
                    pawn.write(SetMapFlag(255, 255))
                }
            }
            TODO("walk(this, pawn, other, ITEM_USE_OPCODE, lineOfSightRange) || Wip")
        }
    }
}
