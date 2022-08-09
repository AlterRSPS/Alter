package gg.rsmod.plugins.content.skills.fishing.data

import gg.rsmod.game.model.Tile

/**
 * @author Fritz <frikkipafi@gmail.com>
 */

class FishingLocation {
    var spots: MutableList<FishingSpot> = mutableListOf()
    var numberOfFishingSpots = 1

    fun register() {
        FishingManager.fishing_locations.add(this)
    }
}