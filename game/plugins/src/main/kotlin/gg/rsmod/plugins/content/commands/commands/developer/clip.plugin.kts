import gg.rsmod.game.model.priv.Privilege

on_command("clip", Privilege.DEV_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val matrix = chunk.getMatrix(player.tile.height)
    val lx = player.tile.x % 8
    val lz = player.tile.z % 8
    player.message("Tile flags: ${chunk.getMatrix(player.tile.height).get(lx, lz)}")
    Direction.RS_ORDER.forEach { dir ->
        val walkBlocked = matrix.isBlocked(lx, lz, dir, projectile = false)
        val projectileBlocked = matrix.isBlocked(lx, lz, dir, projectile = true)
        val walkable = if (walkBlocked) "<col=801700>blocked</col>" else "<col=178000>walkable</col>"
        val projectile = if (projectileBlocked) "<col=801700>projectiles blocked" else "<col=178000>projectiles allowed"
        player.message("$dir: $walkable - $projectile")
    }
}