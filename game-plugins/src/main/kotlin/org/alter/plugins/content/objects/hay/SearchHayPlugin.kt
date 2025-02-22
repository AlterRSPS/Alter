package org.alter.plugins.content.objects.hay

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
import org.alter.rscm.RSCM.getRSCM

class SearchHayPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        val HAY_OBJECTS =
            setOf(
                "object.haystack",
                "object.hay_bales",
                "object.hay_bales_299",
            )

        HAY_OBJECTS.forEach { hay ->
            onObjOption(obj = hay, option = "search") {
                val obj = player.getInteractingGameObj()
                val name = obj.getDef().name
                player.queue {
                    search(this, player, name!!.lowercase())
                }
            }
        }
    }

    suspend fun search(
        it: QueueTask,
        p: Player,
        obj: String,
    ) {
        p.lock()
        p.message("You search the $obj...")
        p.animate(827)
        it.wait(3)
        p.unlock()
        when (world.random(100)) {
            0 -> {
                val add = p.inventory.add(item = "item.needle")
                if (add.hasFailed()) {
                    world.spawn(GroundItem(item = getRSCM("item.needle"), amount = 1, tile = p.tile, owner = p))
                }
                it.chatPlayer(p, "Wow! A needle!<br>Now what are the chances of finding that?")
            }
            1 -> {
                p.hit(damage = 1)
                p.forceChat("Ouch!")
            }
            else -> p.message("You find nothing of interest.")
        }
    }
}
