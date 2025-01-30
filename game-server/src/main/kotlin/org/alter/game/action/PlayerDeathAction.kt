package org.alter.game.action

import dev.openrune.cache.CacheManager.getAnim
import net.rsprot.protocol.game.outgoing.sound.MidiJingle
import org.alter.game.model.attr.KILLER_ATTR
import org.alter.game.model.entity.Player
import org.alter.game.model.move.moveTo
import org.alter.game.model.move.stopMovement
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.queue.TaskPriority
import org.alter.game.plugin.Plugin
import org.alter.game.service.log.LoggerService
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerDeathAction {
    private const val DEATH_ANIMATION = 836

    val deathPlugin: Plugin.() -> Unit = {
        val player = ctx as Player

        player.interruptQueues()
        player.stopMovement()
        player.lock()

        player.queue(TaskPriority.STRONG) {
            death(player)
        }
    }

    private suspend fun QueueTask.death(player: Player) {
        val world = player.world
        val deathAnim = getAnim(DEATH_ANIMATION)
        val instancedMap = world.instanceAllocator.getMap(player.tile)
        player.write(MidiJingle(90))
        player.damageMap.getMostDamage()?.let { killer ->
            if (killer is Player) {
                world.getService(LoggerService::class.java, searchSubclasses = true)?.logPlayerKill(killer, player)
            }
            player.attr[KILLER_ATTR] = WeakReference(killer)
        }

        world.plugins.executePlayerPreDeath(player)
        player.resetFacePawn()
        wait(2)
        player.animate(deathAnim.id)
        wait(deathAnim.cycleLength + 1)
        player.getSkills().restoreAll()
        player.animate(-1)
        if (instancedMap == null) {
            // Note: maybe add a player attribute for death locations
            player.moveTo(player.world.gameContext.home)
        } else {
            player.moveTo(instancedMap.exitTile)
            world.instanceAllocator.death(player)
        }
        player.writeMessage("Oh dear, you are dead!")
        player.unlock()

        player.attr.removeIf { it.resetOnDeath }
        player.timers.removeIf { it.resetOnDeath }

        world.plugins.executePlayerDeath(player)
    }
}
