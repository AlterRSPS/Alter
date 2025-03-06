package org.alter.plugins.content.interfaces.gameframe.world_map

import org.alter.api.InterfaceDestination
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Sound
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.type.interfacedsl.InterfaceFlag
import org.alter.plugins.content.interfaces.worldmap.WorldMap.LAST_TILE
import org.alter.plugins.content.interfaces.worldmap.WorldMap.UPDATE_TIMER
import org.alter.plugins.content.interfaces.worldmap.WorldMap.WORLD_MAP_INTERFACE_ID

class WorldMapPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onButton(interfaceId = 160, component = 53) {
            if (!player.lock.canInterfaceInteract()) {
                return@onButton
            }
            if (!player.isInterfaceVisible(WORLD_MAP_INTERFACE_ID)) {
                val opt = player.getInteractingOption()
                player.sendWorldMapTile()
                player.playSound(Sound.INTERFACE_SELECT1, 100)

                if (opt != 1) {
                    player.openInterface(interfaceId = WORLD_MAP_INTERFACE_ID, dest = InterfaceDestination.WORLD_MAP, fullscreen = false)
                    player.setInterfaceEvents(interfaceId = WORLD_MAP_INTERFACE_ID, component = 21, range = 0..4, setting = InterfaceFlag.ClickOp1)
                } else {
                    player.queue {
                        player.animate(Animation.LOOK_AT_MINIMAP_WHEN_FULLSCREEN)
                        wait(1)
                        player.message("Fullscreen minimap was temporarily disabled.")
                        player.animate(Animation.CLOSE_MINIMAP_FULLSCREEN)
                    }
                }
                player.timers[UPDATE_TIMER] = 1
            } else {
                player.closeInterface(WORLD_MAP_INTERFACE_ID)
            }
        }

        /**
         * Esc key / 'x' closes.
         */
        onButton(interfaceId = WORLD_MAP_INTERFACE_ID, 4, 38) {
            player.closeInterface(WORLD_MAP_INTERFACE_ID)
            player.openOverlayInterface(player.interfaces.displayMode)
            player.attr.remove(LAST_TILE)
            player.timers.remove(UPDATE_TIMER)
        }

        onTimer(UPDATE_TIMER) {
            if (player.isInterfaceVisible(WORLD_MAP_INTERFACE_ID)) {
                /*
                 * Only send the world when the last tile recorded is not the same as
                 * the current one being stood on, so we're not needlessly sending the
                 * script every cycle.
                 */
                val lastTile = player.attr[LAST_TILE]
                if (lastTile == null || !lastTile.sameAs(player.tile)) {
                    player.sendWorldMapTile()
                    player.attr[LAST_TILE] = player.tile
                }

                player.timers[UPDATE_TIMER] = 1
            }
        }
    }
}
