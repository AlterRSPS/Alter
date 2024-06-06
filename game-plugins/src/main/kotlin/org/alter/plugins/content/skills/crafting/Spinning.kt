package org.alter.plugins.content.skills.crafting

import org.alter.api.Skills
import org.alter.api.ext.filterableMessage
import org.alter.api.ext.player
import org.alter.game.model.LockState
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.skills.crafting.data.Spin

object Spinning {
    suspend fun Spin(
        it: QueueTask,
        Spin: Spin,
    ) {
        if (!canSpin(it.player, Spin)) {
            return
        }
        val player = it.player
        val inventory = player.inventory
        val removeunspun = inventory.remove(item = Spin.unSpun)

        if (removeunspun.hasSucceeded()) {
            inventory.add(Spin.id)
        }

        while (true) {
            player.lock = LockState.DELAY_ACTIONS
            player.addXp(Skills.CRAFTING, Spin.craftXp)
            player.animate(Spin.animation)
            it.wait(5)
            player.lock = LockState.NONE

            if (!canSpin(player, Spin)) {
                player.animate(-1)
                break
            }
            player.animate(-1)

            break
        }
        it.wait(1)
    }
}

private fun canSpin(
    player: Player,
    Spin: Spin,
): Boolean {
    if (player.getSkills().getCurrentLevel(Skills.CRAFTING) < Spin.level) {
        player.filterableMessage("You need a Crafting level of ${Spin.level} to spin ${Spin.prefix}.")
        return false
    }

    return true
}
