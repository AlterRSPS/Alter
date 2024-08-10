package org.alter.plugins.content.interfaces.equipstats

import org.alter.api.BonusSlot
import org.alter.api.ClientScript
import org.alter.api.cfg.Items
import org.alter.api.ext.*
import org.alter.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EquipmentStats {
    const val EQUIPMENTSTATS_INTERFACE_ID = 84
    const val EQUIPMENTSTATS_TAB_INTERFACE_ID = 85

    private val MAGE_ELITE_VOID = intArrayOf(Items.VOID_MAGE_HELM, Items.ELITE_VOID_TOP, Items.ELITE_VOID_ROBE, Items.VOID_KNIGHT_GLOVES)

    /**
     * @TODO
     * Need to move these two methods [formatBonus, formatBonus] outside for reuse.
     */
    fun formatBonus(
        p: Player,
        slot: BonusSlot,
    ): String = formatBonus(p.getBonus(slot))

    fun formatBonus(bonus: Int): String = if (bonus < 0) bonus.toString() else "+$bonus"

    private fun formatBonus(bonus: Double): String {
        val format = String.format("%.1f", bonus)
        return if (bonus < 0) format else "+$format"
    }

    fun sendBonuses(p: Player) {
        // TODO: these two bonuses
        var undeadBonus = 0.0
        var slayerBonus = 0.0

        /**
         * Magic and other bonuses belong inside Player class
         */
        var magicDamageBonus = p.getMagicDamageBonus().toDouble()

        if (p.hasEquipped(MAGE_ELITE_VOID)) {
            magicDamageBonus += 2.5
        }

        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 23,
            text = "Stab: ${formatBonus(p, BonusSlot.ATTACK_STAB)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 24,
            text = "Slash: ${formatBonus(p, BonusSlot.ATTACK_SLASH)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 25,
            text = "Crush: ${formatBonus(p, BonusSlot.ATTACK_CRUSH)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 26,
            text = "Magic: ${formatBonus(p, BonusSlot.ATTACK_MAGIC)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 27,
            text = "Range: ${formatBonus(p, BonusSlot.ATTACK_RANGED)}",
        )
        p.setComponentText(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = 52, text = "Base: 2.4s") // @TODO Normal Weapon Attack speed
        p.setComponentText(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = 53, text = "Actual: 2.4s") // @TODO Attack speed with rapid and etc.
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 29,
            text = "Stab: ${formatBonus(p, BonusSlot.DEFENCE_STAB)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 30,
            text = "Slash: ${formatBonus(p, BonusSlot.DEFENCE_SLASH)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 31,
            text = "Crush: ${formatBonus(p, BonusSlot.DEFENCE_CRUSH)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 33,
            text = "Range: ${formatBonus(p, BonusSlot.DEFENCE_RANGED)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 32,
            text = "Magic: ${formatBonus(p, BonusSlot.DEFENCE_MAGIC)}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 35,
            text = "Melee STR: ${formatBonus(p.getStrengthBonus())}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 36,
            text = "Ranged STR: ${formatBonus(p.getRangedStrengthBonus())}",
        )
        p.setComponentText(
            interfaceId = EQUIPMENTSTATS_INTERFACE_ID,
            component = 37,
            text = "Magic DMG: ${formatBonus(magicDamageBonus).toDouble()}%",
        )
        p.setComponentText(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = 38, text = "Prayer: ${formatBonus(p.getPrayerBonus())}")

        val undead = String.format("%.1f", undeadBonus)
        val slayer = String.format("%.1f", slayerBonus)

        p.setComponentText(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = 40, text = "Undead: $undead%")
        p.runClientScript(
            ClientScript(id = 7065),
            5505075,
            5505064,
            "Increases your effective accuracy and damage against undead creatures. For multi-target Ranged and Magic attacks, this applies only to the primary target. It does not stack with the Slayer multiplier.",
        )
        // ClientScript(id = 7065, converted = [84:51, 84:40, "Increases your effective accuracy and damage against undead creatures. For multi-target Ranged and Magic attacks, this applies only to the primary target. It does not stack with the Slayer multiplier."], raw = [5505075, 5505064, "Increases your effective accuracy and damage against undead creatures. For multi-target Ranged and Magic attacks, this applies only to the primary target. It does not stack with the Slayer multiplier."], types = [IIs]
        p.setComponentText(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = 41, text = "Slayer: $slayer%")
        // SetEvents()
    }
}
