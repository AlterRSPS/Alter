package org.alter.plugins.content.interfaces.worldmap

import org.alter.game.model.Tile
import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.timer.TimerKey

/**
 * @author Tom <rspsmods@gmail.com>
 */
object WorldMap {
    val UPDATE_TIMER = TimerKey()
    val LAST_TILE = AttributeKey<Tile>()

    const val WORLD_MAP_INTERFACE_ID = 595
    const val WORLD_MAP_FULLSCREEN_INTERFACE_ID = 594
}
