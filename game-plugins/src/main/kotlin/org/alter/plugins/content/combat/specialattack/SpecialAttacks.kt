package org.alter.plugins.content.combat.specialattack

import org.alter.api.EquipmentType
import org.alter.api.ext.getEquipment
import org.alter.game.model.World
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.rscm.RSCM.getRSCM
import org.alter.plugins.content.interfaces.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpecialAttacks {

    fun register(
        item: String,
        energy: Int,
        executeInstantly: Boolean = false,
        attack: CombatContext.() -> Unit,
    ) {
        register(
            getRSCM(item),
            energy,
            executeInstantly,
            attack
        )
    }

    fun register(
        item: Int,
        energy: Int,
        executeInstantly: Boolean = false,
        attack: CombatContext.() -> Unit,
    ) {
        attacks[item] = SpecialAttack(energy, executeInstantly, attack)
    }

    fun executeOnEnable(item: Int): Boolean {
        if (attacks.containsKey(item)) {
            return attacks[item]!!.executeOnSpecBar
        }
        return false
    }

    fun execute(
        player: Player,
        target: Pawn?,
        world: World,
    ): Boolean {
        val weaponItem = player.getEquipment(EquipmentType.WEAPON) ?: return false
        val special = attacks[weaponItem.id] ?: return false

        if (AttackTab.getEnergy(player) < special.energyRequired) {
            return false
        }

        AttackTab.setEnergy(player, AttackTab.getEnergy(player) - special.energyRequired)

        val combatContext = CombatContext(world, player)
        target?.let { combatContext.target = it }
        special.attack(combatContext)

        return true
    }

    val attacks = mutableMapOf<Int, SpecialAttack>()
}
