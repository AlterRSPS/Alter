package org.alter.plugins.content.interfaces.equipstats

import org.alter.api.BonusSlot
import org.alter.api.ClientScript
import org.alter.api.ext.*
import org.alter.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EquipmentStats {
    const val EQUIPMENTSTATS_INTERFACE_ID = 84
    const val EQUIPMENTSTATS_TAB_INTERFACE_ID = 85

    private val MAGE_ELITE_VOID = arrayOf("item.void_mage_helm", "item.elite_void_top", "item.elite_void_robe", "item.void_knight_gloves")

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
        p.setInterfaceEvents(
            interfaceId = 85,
            component = 0,
            range = 0..27,
            setting = arrayOf(InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp10, InterfaceEvent.DRAG_DEPTH1, InterfaceEvent.DragTargetable)
        )
        with(p) {
            setEquipCompText(component = 24, text = bonusTextMap()[0])
            setEquipCompText(component = 25, text = bonusTextMap()[1])
            setEquipCompText(component = 26, text = bonusTextMap()[2])
            setEquipCompText(component = 27, text = bonusTextMap()[3])
            setEquipCompText(component = 28, text = bonusTextMap()[4])
            setEquipCompText(component = 53, text = bonusTextMap()[5])
            setEquipCompText(component = 54, text = bonusTextMap()[6])
            setEquipCompText(component = 30, text = bonusTextMap()[7])
            setEquipCompText(component = 31, text = bonusTextMap()[8])
            setEquipCompText(component = 32, text = bonusTextMap()[9])
            setEquipCompText(component = 34, text = bonusTextMap()[10])
            setEquipCompText(component = 33, text = bonusTextMap()[11])
            setEquipCompText(component = 36, text = bonusTextMap()[12])
            setEquipCompText(component = 37, text = bonusTextMap()[13])
            setEquipCompText(component = 38, text = bonusTextMap()[14])
            setEquipCompText(component = 39, text = bonusTextMap()[15])
            setEquipCompText(component = 41, text = bonusTextMap()[16])
            setEquipCompText(component = 42, text = bonusTextMap()[17])
            runClientScript(
                ClientScript(id = 7065),
                5505075,
                5505064,
                "Increases your effective accuracy and damage against undead creatures. For multi-target Ranged and Magic attacks, this applies only to the primary target. It does not stack with the Slayer multiplier."
            )
        }
    }

    fun Player.bonusTextMap(): List<String> {
        var magicDamageBonus = getMagicDamageBonus().toDouble()
        return listOf(
            "Stab: ${formatBonus(this, BonusSlot.ATTACK_STAB)}",
            "Slash: ${formatBonus(this, BonusSlot.ATTACK_SLASH)}",
            "Crush: ${formatBonus(this, BonusSlot.ATTACK_CRUSH)}",
            "Magic: ${formatBonus(this, BonusSlot.ATTACK_MAGIC)}",
            "Range: ${formatBonus(this, BonusSlot.ATTACK_RANGED)}",
            "Base: TODO",
            "Actual: TODO",
            "Stab: ${formatBonus(this, BonusSlot.DEFENCE_STAB)}",
            "Slash: ${formatBonus(this, BonusSlot.DEFENCE_SLASH)}",
            "Crush: ${formatBonus(this, BonusSlot.DEFENCE_CRUSH)}",
            "Range: ${formatBonus(this, BonusSlot.DEFENCE_RANGED)}",
            "Magic: ${formatBonus(this, BonusSlot.DEFENCE_MAGIC)}",
            "Melee STR: ${formatBonus(this.getStrengthBonus())}",
            "Ranged STR: ${formatBonus(this.getRangedStrengthBonus())}",
            "Magic DMG: ${formatBonus(magicDamageBonus).toDouble()}%",
            "Prayer: ${formatBonus(this.getPrayerBonus())}",
            "Undead: TODO",
            "Slayer: TODO"
        )
    }

    private fun Player.setEquipCompText(component: Int, text: String) {
        this.setComponentText(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = component, text = text)
    }
}
