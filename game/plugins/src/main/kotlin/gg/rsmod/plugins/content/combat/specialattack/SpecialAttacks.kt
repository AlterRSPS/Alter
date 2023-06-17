package gg.rsmod.plugins.content.combat.specialattack

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.ext.getEquipment
import gg.rsmod.plugins.content.interfaces.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpecialAttacks {

    fun register(item: Int, energy: Int, executeOnSpecBar: Boolean = false, attack: CombatContext.() -> Unit) {
        attacks[item] = SpecialAttack(energy, executeOnSpecBar, attack)
    }

    fun executeOnEnable(item: Int) : Boolean {
        if (attacks.containsKey(item)) {
            return attacks[item]!!.executeOnSpecBar
        }
        return false
    }

    fun execute(player: Player, target: Pawn?, world: World): Boolean {

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