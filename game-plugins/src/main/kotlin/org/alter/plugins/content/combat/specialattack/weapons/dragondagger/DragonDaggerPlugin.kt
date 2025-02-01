package org.alter.plugins.content.combat.specialattack.weapons.dragondagger

import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.AreaSound
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.specialattack.SpecialAttacks

class DragonDaggerPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {

        val SPECIAL_REQUIREMENT = 25

        SpecialAttacks.register("item.dragon_dagger", SPECIAL_REQUIREMENT) {
            player.animate(id = 1062)
            player.graphic(id = 252, height = 92)
            world.spawn(AreaSound(tile = player.tile, id = 2537, radius = 10, volume = 1))

            for (i in 0 until 2) {
                val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.15)
                val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.25)
                val landHit = accuracy >= world.randomDouble()
                val delay = if (target.entityType.isNpc) i + 1 else 1
                player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
            }
        }
    }
}
