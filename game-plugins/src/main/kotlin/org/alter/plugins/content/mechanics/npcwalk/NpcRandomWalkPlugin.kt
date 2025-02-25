package org.alter.plugins.content.mechanics.npcwalk

import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.move.walkTo
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class NpcRandomWalkPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        val routeSearchTimer = TimerKey()
        val routeSearchDelay = 15..30 // TODO: Find a way to override this for certain NPCs?

        onGlobalNpcSpawn {
            if (npc.walkRadius > 0) {
                npc.timers[routeSearchTimer] = world.random(routeSearchDelay)
            }
        }

        onTimer(routeSearchTimer) {
            if (npc.isActive() && npc.lock.canMove()) {
                val facing = npc.attr[FACING_PAWN_ATTR]?.get()

                // The npc is not facing a player, so it can walk.
                if (facing == null) {
                    val rx = world.random(-npc.walkRadius..npc.walkRadius)
                    val rz = world.random(-npc.walkRadius..npc.walkRadius)

                    val start = npc.spawnTile
                    val dest = start.transform(rx, rz)

                    // Only walk to destination if the chunk has previously been created.
                    if (world.chunks.get(dest, createIfNeeded = false) != null) {
                        npc.walkTo(dest)
                    }
                }
            }

            npc.timers[routeSearchTimer] = world.random(routeSearchDelay)
        }
    }
}
