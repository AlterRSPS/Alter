package org.alter.plugins.content.skills.crafting
/**
 *TO-DO
 * crushed gem for OPAL JADE & REDTOPAZ in a interpolate
 * abilty to change "you need crafting lvl to cut this" to "you need crafting lvl to cut "gemname"" same with cutting gem
 * rest of crafting this is just basic gem cutting
 */
import org.alter.game.model.LockState
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.api.Skills
import org.alter.api.ext.filterableMessage
import org.alter.api.ext.player
import org.alter.plugins.content.skills.crafting.data.Gems

object Cutting {

    suspend fun gemCut(it: QueueTask, gem: Gems) {
        if(!canCut(it.player, gem)) {
            return
        }
        val player = it.player
        val inventory = player.inventory
        val removeuncut = inventory.remove(item = gem.uncutGem)

        if (removeuncut.hasSucceeded()) {
            inventory.add(gem.id)
        }

        player.filterableMessage("You cut the ${gem.prefix}.")

        while(true) {
            player.lock = LockState.DELAY_ACTIONS
            player.addXp(Skills.CRAFTING, gem.craftXp)
            player.animate(gem.animation)
            it.wait(5)
            player.lock = LockState.NONE

            if(!canCut(player,gem)) {
                player.animate(-1)
                break
            }
                player.animate(-1)

                break
            }
            it.wait(1)
        }
    }

    private fun canCut(player: Player, gem: Gems): Boolean {
        if(player.getSkills().getCurrentLevel(Skills.CRAFTING) < gem.level) {
            player.filterableMessage("You need a Crafting level of ${gem.level} to cut ${gem.prefix}.")
            return false
        }

        return true
    }