package gg.rsmod.plugins.content.combat.weapons

import gg.rsmod.plugins.content.combat.dealHit
/**
 * @author CloudS3c 3/17/2023
 */
set_weapon_combat_logic(Items.SCYTHE_OF_VITUR_UNCHARGED) {
    val target = player.getTarget()
    var gfx: Int = 0
    target?.let {
        when(player.lastFacingDirection) {
            Direction.NORTH -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_NORTH
            Direction.EAST -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_EAST
            Direction.WEST -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_WEST
            Direction.SOUTH -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_SOUTH
            else -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_NORTH
        }
        player.message("${player.lastFacingDirection}")
        player.animate(Animation.HUMAN_SCYTHE_OF_VITUR_ATTACK)
        player.playSound(Sounds.SCYTHE_SLASH)
        for (x in 1..3) {
            for (tileX in it.tile.x-1..it.tile.x+1) {
                val npc = getNpcFromTile(Tile(tileX, target.tile.z))
                npc?.let {
                    player.dealHit(target = it, maxHit = 10, landHit = true, delay = 1)
                    it.graphic(Graphics.FLAMES_OF_ZAMORAK)
                }
            }
        }
        world.spawn(TileGraphic(target.tile, gfx, 96))
    }
}
