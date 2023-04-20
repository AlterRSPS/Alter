import gg.rsmod.game.model.priv.Privilege

arrayOf("mypos", "coords", "pos").forEach { pos ->

on_command(pos, Privilege.ADMIN_POWER, description = "Your position in coordinates") {
    val instancedMap = world.instanceAllocator.getMap(player.tile)
    val tile = player.tile
    if (instancedMap == null) {
        player.message("Tile=[<col=801700>x: ${tile.x}, z: ${tile.z}, height: ${tile.height}</col>], Region=${player.tile.regionId}")
    } else {
        val delta = tile - instancedMap.area.bottomLeft
        player.message("Tile=[<col=801700>x: ${tile.x}, z: ${tile.z}, height: ${tile.height}</col>], Relative=[${delta.x}, ${delta.z}]")
    }
}
}