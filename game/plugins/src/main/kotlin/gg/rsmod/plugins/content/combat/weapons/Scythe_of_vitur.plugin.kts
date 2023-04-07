package gg.rsmod.plugins.content.combat.weapons

import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.getCombatTarget

/**
 * @author CloudS3c 3/17/2023
 *
 * This is experimental plugin.. Not some live replica..
 */
set_item_combat_logic(Items.SCYTHE_OF_VITUR_UNCHARGED) {
    val target = player.getTarget()
    println(target)
    var gfx: Int
    target?.let {
        when(player.lastFacingDirection) {
            Direction.WEST -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_EAST
            Direction.EAST -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_WEST // ?
            Direction.SOUTH -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_NORTH
            Direction.NORTH_WEST -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_SOUTH // ?
            Direction.NORTH_EAST -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_SOUTH // ?
            else -> gfx = Graphics.DRAGON_HALBERD_SPECIAL_NORTH
        }
        player.message("${player.lastFacingDirection}")
        player.animate(Animation.HUMAN_SCYTHE_OF_VITUR_ATTACK)
        player.playSound(Sounds.SCYTHE_SLASH)
        for (x in 1..3) {
            player.dealHit(target = it, maxHit = 90, landHit = true, delay = 1)
        }
        world.spawn(TileGraphic(target.tile, gfx, 96, 20))
    }
}
/**
 * How does the animation when npc attacks player --> Which anim gets executed first. Need better understanding in this.
 * Special code when npc is under you
 *
 * Uhh unsure. [OSRS]: When player gets hit from (npcs script) He does not do the block animation,
 * But when it's melee attack he does the block animation. (Src: https://www.youtube.com/watch?v=sZ3bs8blZU8)
 *
 */
set_item_combat_logic(Items.DRAGON_HUNTER_LANCE) {
    //val defs = GetItemDef(Items.DRAGON_HUNTER_LANCE)
    val target = player.getCombatTarget()
    target?.let {
        if (target is Npc) {
        var combatDefs = getNpcCombatDef(target.id)
            if (combatDefs != null) {
                println("Npc Combat Defs arent null")
                val DragonSpecieList = listOf(
                    NpcSpecies.BASIC_DRAGON,
                    NpcSpecies.DRAGON,
                    NpcSpecies.BRUTAL_DRAGON,
                )
                if (combatDefs.species.any(DragonSpecieList::contains)) {
                    println("Npc does have Species")
                    player.dealHit(target = it, maxHit = 9999, landHit = true, delay = 1)
                } else {
                    println("Npc Does not have")
                }
            }
        }
    }
}