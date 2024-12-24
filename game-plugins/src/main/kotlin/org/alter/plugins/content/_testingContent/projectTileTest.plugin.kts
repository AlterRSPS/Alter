import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.DragonfireFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy
import org.alter.plugins.content.mechanics.poison.poison

on_command("pt") {
    val tiles = mutableListOf(
        Tile(player.tile.transform(x = 10, z = 0)),
        Tile(player.tile.transform(x = -10, z = 0)),
        Tile(player.tile.transform(x = 0, z = 10)),
        Tile(player.tile.transform(x = 0, z = -10)),
    )

    tiles.forEach {
        if (pawn is Player) {
            val pT: Projectile = Projectile.Builder()
                .setTiles(start = it, target = player)
                .setGfx(2559)
                .setHeights(startHeight = 31, endHeight = 0)
                .setSlope(angle = 15, steepness = 222)
                .setTimes(20, lifespan = 50).build()
            world.spawn(pT)
            val hit =
                player.dealHit(
                    target = player,
                    formula = DragonfireFormula(maxHit = 1, minHit = 1),
                    delay = RangedCombatStrategy.getHitDelay(
                        player.getFrontFacingTile(player),
                        player.getCentreTile()
                    ) - 1,
                ) {
                    if (it.landed() && world.chance(1, 6)) {
                        player.graphic(2556, 20)
                    }
                }
            if (hit.blocked()) {
                player.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
            }
        }
    }
}