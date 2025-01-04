package org.alter.plugins.content.combat

import org.alter.api.EquipmentType
import org.alter.api.WeaponType
import org.alter.api.ext.getAttackStyle
import org.alter.api.ext.getEquipment
import org.alter.api.ext.hasEquipped
import org.alter.api.ext.hasWeaponType
import org.alter.game.model.combat.*
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.plugins.content.combat.strategy.CombatStrategy
import org.alter.plugins.content.combat.strategy.MagicCombatStrategy
import org.alter.plugins.content.combat.strategy.MeleeCombatStrategy
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy

/**
 * @author Tom <rspsmods@gmail.com>
 */
object CombatConfigs {
    private const val PLAYER_DEFAULT_ATTACK_SPEED = 4

    private const val MIN_ATTACK_SPEED = 1

    private val DEFENDERS =
        arrayOf(
            "item.bronze_defender",
            "item.iron_defender",
            "item.steel_defender",
            "item.mithril_defender",
            "item.black_defender",
            "item.adamant_defender",
            "item.rune_defender",
            "item.dragon_defender",
            "item.dragon_defender_t",
            "item.avernic_defender",
        )

    private val BOOKS =
        arrayOf(
            "item.holy_book",
            "item.book_of_balance",
            "item.unholy_book",
            "item.book_of_law",
            "item.book_of_war",
            "item.book_of_darkness",
            "item.mages_book",
            "item.tome_of_fire",
            "item.tome_of_fire_empty",
        )

    private val BOXING_GLOVES =
        arrayOf(
            "item.boxing_gloves",
            "item.boxing_gloves_7673",
            "item.beach_boxing_gloves",
            "item.beach_boxing_gloves_11706",
        )

    private val GODSWORDS =
        arrayOf(
            "item.armadyl_godsword",
            "item.armadyl_godsword_or",
            "item.bandos_godsword",
            "item.bandos_godsword_or",
            "item.saradomin_godsword",
            "item.saradomin_godsword_or",
            "item.zamorak_godsword",
            "item.zamorak_godsword_or",
        )

    fun getCombatStrategy(pawn: Pawn): CombatStrategy =
        when (getCombatClass(pawn)) {
            CombatClass.MELEE -> MeleeCombatStrategy
            CombatClass.MAGIC -> MagicCombatStrategy
            CombatClass.RANGED -> RangedCombatStrategy
            else -> throw IllegalStateException("Invalid combat class: ${getCombatClass(pawn)} for $pawn")
        }

    fun getCombatClass(pawn: Pawn): CombatClass {
        if (pawn is Npc) {
            return pawn.combatClass
        }

        if (pawn is Player) {
            return when {
                pawn.attr.has(Combat.CASTING_SPELL) -> CombatClass.MAGIC
                pawn.hasWeaponType(WeaponType.BOW, WeaponType.CHINCHOMPA, WeaponType.CROSSBOW, WeaponType.THROWN) -> CombatClass.RANGED
                else -> CombatClass.MELEE
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getAttackDelay(pawn: Pawn): Int {
        if (pawn is Npc) {
            return pawn.combatDef.attackSpeed
        }

        if (pawn is Player) {
            val default = PLAYER_DEFAULT_ATTACK_SPEED
            val weapon = pawn.getEquipment(EquipmentType.WEAPON) ?: return default
            return Math.max(MIN_ATTACK_SPEED, weapon.getDef().attackSpeed)
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getCombatDef(pawn: Pawn): NpcCombatDef? {
        if (pawn is Npc) {
            return pawn.combatDef
        }
        return null
    }

    fun getAttackAnimation(pawn: Pawn): Int {
        if (pawn is Npc) {
            return pawn.combatDef.attackAnimation
        }

        if (pawn is Player) {
            val style = pawn.getAttackStyle()

            return when {
                pawn.hasEquipped(EquipmentType.WEAPON, *GODSWORDS) -> 7045
                pawn.hasWeaponType(WeaponType.AXE) -> if (style == 1) 401 else 395
                pawn.hasWeaponType(WeaponType.HAMMER) -> 401
                pawn.hasWeaponType(WeaponType.BULWARK) -> 7511
                pawn.hasWeaponType(WeaponType.SCYTHE) -> 8056
                pawn.hasWeaponType(WeaponType.BOW) -> 426
                pawn.hasWeaponType(WeaponType.CROSSBOW) -> 4230
                pawn.hasWeaponType(WeaponType.LONG_SWORD) -> if (style == 2) 386 else 390
                pawn.hasWeaponType(WeaponType.TWO_HANDED) -> if (style == 2) 406 else 407
                pawn.hasWeaponType(WeaponType.PICKAXE) -> if (style == 2) 400 else 401
                pawn.hasWeaponType(WeaponType.DAGGER) -> if (style == 2) 390 else 386
                pawn.hasWeaponType(WeaponType.MAGIC_STAFF) || pawn.hasWeaponType(WeaponType.STAFF) -> 419
                pawn.hasWeaponType(WeaponType.MACE) -> if (style == 2) 400 else 401
                pawn.hasWeaponType(WeaponType.CHINCHOMPA) -> 7618
                pawn.hasWeaponType(WeaponType.THROWN) -> if (pawn.hasEquipped(EquipmentType.WEAPON, "item.toktzxilul")) 7558 else 929
                pawn.hasWeaponType(WeaponType.WHIP) -> 1658
                pawn.hasWeaponType(WeaponType.SPEAR) || pawn.hasWeaponType(WeaponType.HALBERD) ->
                    if (style == 1) {
                        440
                    } else if (style == 2) {
                        429
                    } else {
                        428
                    }
                pawn.hasWeaponType(WeaponType.CLAWS) -> 393
                else -> if (style == 1) 423 else 422
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getBlockAnimation(pawn: Pawn): Int {
        if (pawn is Npc) {
            return pawn.combatDef.blockAnimation
        }

        if (pawn is Player) {
            return when {
                pawn.hasEquipped(EquipmentType.SHIELD, *BOOKS) -> 420
                pawn.hasEquipped(EquipmentType.WEAPON, "item.sled_4084") -> 1466
                pawn.hasEquipped(EquipmentType.WEAPON, "item.easter_basket") -> 1834
                pawn.hasEquipped(EquipmentType.SHIELD, *DEFENDERS) -> 4177
                pawn.getEquipment(EquipmentType.SHIELD) != null -> 1156 // If wearing any shield, this animation is used

                pawn.hasEquipped(EquipmentType.WEAPON, *BOXING_GLOVES) -> 3679
                pawn.hasEquipped(EquipmentType.WEAPON, *GODSWORDS) -> 7056
                pawn.hasEquipped(EquipmentType.WEAPON, "item.light_ballista", "item.heavy_ballista") -> 7219
                pawn.hasEquipped(EquipmentType.WEAPON, "item.zamorakian_spear") -> 1709

                pawn.hasWeaponType(WeaponType.DAGGER) -> 378
                pawn.hasWeaponType(WeaponType.LONG_SWORD) -> 388
                pawn.hasWeaponType(WeaponType.PICKAXE, WeaponType.CLAWS) -> 397
                pawn.hasWeaponType(WeaponType.MACE) -> 403
                pawn.hasWeaponType(WeaponType.TWO_HANDED) -> 410
                pawn.hasWeaponType(WeaponType.MAGIC_STAFF) -> 420
                pawn.hasWeaponType(WeaponType.BOW) -> 424
                pawn.hasWeaponType(WeaponType.SPEAR, WeaponType.HALBERD) -> 430
                pawn.hasWeaponType(WeaponType.WHIP) -> 1659
                pawn.hasWeaponType(WeaponType.BULWARK) -> 7512
                else -> 424
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getAttackStyle(pawn: Pawn): AttackStyle {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).attackStyle
        }

        if (pawn is Player) {
            val style = pawn.getAttackStyle()

            return when {
                pawn.hasWeaponType(WeaponType.NONE) ->
                    when (style) {
                        0 -> AttackStyle.ACCURATE
                        1 -> AttackStyle.AGGRESSIVE
                        3 -> AttackStyle.DEFENSIVE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.BOW, WeaponType.CROSSBOW, WeaponType.THROWN, WeaponType.CHINCHOMPA) ->
                    when (style) {
                        0 -> AttackStyle.ACCURATE
                        1 -> AttackStyle.RAPID
                        3 -> AttackStyle.LONG_RANGE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.TRIDENT) ->
                    when (style) {
                        0, 1 -> AttackStyle.ACCURATE
                        3 -> AttackStyle.LONG_RANGE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(
                    WeaponType.AXE,
                    WeaponType.HAMMER,
                    WeaponType.TWO_HANDED,
                    WeaponType.PICKAXE,
                    WeaponType.DAGGER,
                    WeaponType.MAGIC_STAFF,
                    WeaponType.LONG_SWORD,
                    WeaponType.MAGIC_STAFF,
                    WeaponType.CLAWS,
                ) ->
                    when (style) {
                        0 -> AttackStyle.ACCURATE
                        1 -> AttackStyle.AGGRESSIVE
                        2 -> AttackStyle.CONTROLLED
                        3 -> AttackStyle.DEFENSIVE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.SPEAR) ->
                    when (style) {
                        3 -> AttackStyle.DEFENSIVE
                        else -> AttackStyle.CONTROLLED
                    }

                pawn.hasWeaponType(WeaponType.HALBERD) ->
                    when (style) {
                        0 -> AttackStyle.CONTROLLED
                        1 -> AttackStyle.AGGRESSIVE
                        3 -> AttackStyle.DEFENSIVE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.SCYTHE) ->
                    when (style) {
                        0 -> AttackStyle.ACCURATE
                        1 -> AttackStyle.AGGRESSIVE
                        2 -> AttackStyle.AGGRESSIVE
                        3 -> AttackStyle.DEFENSIVE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.WHIP) ->
                    when (style) {
                        0 -> AttackStyle.ACCURATE
                        1 -> AttackStyle.CONTROLLED
                        3 -> AttackStyle.DEFENSIVE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.BLUDGEON) ->
                    when (style) {
                        0, 1, 3 -> AttackStyle.AGGRESSIVE
                        else -> AttackStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.BULWARK) -> AttackStyle.ACCURATE

                else -> AttackStyle.NONE
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getCombatStyle(pawn: Pawn): CombatStyle {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).combatStyle
        }

        if (pawn is Player) {
            val style = pawn.getAttackStyle()

            return when {
                pawn.attr.has(Combat.CASTING_SPELL) -> CombatStyle.MAGIC

                pawn.hasWeaponType(WeaponType.NONE) ->
                    when (style) {
                        0 -> CombatStyle.CRUSH
                        1 -> CombatStyle.CRUSH
                        3 -> CombatStyle.CRUSH
                        else -> CombatStyle.NONE
                    }

                pawn.hasWeaponType(WeaponType.BOW, WeaponType.CROSSBOW, WeaponType.THROWN, WeaponType.CHINCHOMPA) -> CombatStyle.RANGED

                pawn.hasWeaponType(WeaponType.AXE) ->
                    when (style) {
                        2 -> CombatStyle.CRUSH
                        else -> CombatStyle.SLASH
                    }

                pawn.hasWeaponType(WeaponType.HAMMER) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.CLAWS) ->
                    when (style) {
                        2 -> CombatStyle.STAB
                        else -> CombatStyle.SLASH
                    }

                pawn.hasWeaponType(WeaponType.SALAMANDER) ->
                    when (style) {
                        0 -> CombatStyle.SLASH
                        1 -> CombatStyle.RANGED
                        else -> CombatStyle.MAGIC
                    }

                pawn.hasWeaponType(WeaponType.LONG_SWORD) ->
                    when (style) {
                        2 -> CombatStyle.STAB
                        else -> CombatStyle.SLASH
                    }

                pawn.hasWeaponType(WeaponType.TWO_HANDED) ->
                    when (style) {
                        2 -> CombatStyle.CRUSH
                        else -> CombatStyle.SLASH
                    }

                pawn.hasWeaponType(WeaponType.PICKAXE) ->
                    when (style) {
                        2 -> CombatStyle.CRUSH
                        else -> CombatStyle.STAB
                    }

                pawn.hasWeaponType(WeaponType.HALBERD) ->
                    when (style) {
                        1 -> CombatStyle.SLASH
                        else -> CombatStyle.STAB
                    }

                pawn.hasWeaponType(WeaponType.STAFF) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.SCYTHE) ->
                    when (style) {
                        2 -> CombatStyle.CRUSH
                        else -> CombatStyle.SLASH
                    }

                pawn.hasWeaponType(WeaponType.SPEAR) ->
                    when (style) {
                        1 -> CombatStyle.SLASH
                        2 -> CombatStyle.CRUSH
                        else -> CombatStyle.STAB
                    }

                pawn.hasWeaponType(WeaponType.MACE) ->
                    when (style) {
                        2 -> CombatStyle.STAB
                        else -> CombatStyle.CRUSH
                    }

                pawn.hasWeaponType(WeaponType.DAGGER) ->
                    when (style) {
                        2 -> CombatStyle.SLASH
                        else -> CombatStyle.STAB
                    }

                pawn.hasWeaponType(WeaponType.MAGIC_STAFF) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.WHIP) -> CombatStyle.SLASH

                pawn.hasWeaponType(WeaponType.STAFF_HALBERD) ->
                    when (style) {
                        0 -> CombatStyle.STAB
                        1 -> CombatStyle.SLASH
                        else -> CombatStyle.CRUSH
                    }

                pawn.hasWeaponType(WeaponType.TRIDENT) -> CombatStyle.MAGIC

                pawn.hasWeaponType(WeaponType.BLUDGEON) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.BULWARK) ->
                    when (style) {
                        0 -> CombatStyle.CRUSH
                        else -> CombatStyle.NONE
                    }

                else -> CombatStyle.NONE
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getXpMode(player: Player): XpMode {
        val style = player.getAttackStyle()

        return when {
            player.hasWeaponType(WeaponType.NONE) -> {
                when (style) {
                    1 -> XpMode.STRENGTH
                    3 -> XpMode.DEFENCE
                    else -> XpMode.ATTACK
                }
            }

            player.hasWeaponType(
                WeaponType.AXE,
                WeaponType.HAMMER,
                WeaponType.TWO_HANDED,
                WeaponType.PICKAXE,
                WeaponType.DAGGER,
                WeaponType.STAFF,
                WeaponType.MAGIC_STAFF,
            ) -> {
                when (style) {
                    1 -> XpMode.STRENGTH
                    2 -> XpMode.STRENGTH
                    3 -> XpMode.DEFENCE
                    else -> XpMode.ATTACK
                }
            }

            player.hasWeaponType(WeaponType.LONG_SWORD, WeaponType.MACE, WeaponType.CLAWS) -> {
                when (style) {
                    1 -> XpMode.STRENGTH
                    2 -> XpMode.SHARED
                    3 -> XpMode.DEFENCE
                    else -> XpMode.ATTACK
                }
            }

            player.hasWeaponType(WeaponType.WHIP) -> {
                when (style) {
                    1 -> XpMode.SHARED
                    3 -> XpMode.DEFENCE
                    else -> XpMode.ATTACK
                }
            }

            player.hasWeaponType(WeaponType.SPEAR) -> {
                when (style) {
                    3 -> XpMode.DEFENCE
                    else -> XpMode.SHARED
                }
            }

            player.hasWeaponType(WeaponType.TRIDENT) -> {
                when (style) {
                    3 -> XpMode.SHARED
                    else -> XpMode.MAGIC
                }
            }

            player.hasWeaponType(WeaponType.SCYTHE) -> {
                when (style) {
                    0 -> XpMode.ATTACK
                    1 -> XpMode.STRENGTH
                    2 -> XpMode.STRENGTH
                    else -> XpMode.DEFENCE
                }
            }

            player.hasWeaponType(WeaponType.HALBERD) -> {
                when (style) {
                    0 -> XpMode.SHARED
                    1 -> XpMode.STRENGTH
                    else -> XpMode.DEFENCE
                }
            }

            player.hasWeaponType(WeaponType.STAFF_HALBERD) -> {
                when (style) {
                    0 -> XpMode.ATTACK
                    1 -> XpMode.STRENGTH
                    else -> XpMode.DEFENCE
                }
            }

            player.hasWeaponType(WeaponType.BLUDGEON) -> XpMode.STRENGTH

            player.hasWeaponType(WeaponType.BULWARK) -> XpMode.ATTACK

            player.hasWeaponType(WeaponType.BOW, WeaponType.CROSSBOW, WeaponType.THROWN, WeaponType.CHINCHOMPA) -> {
                when (style) {
                    3 -> XpMode.SHARED
                    else -> XpMode.RANGED
                }
            }

            player.hasWeaponType(WeaponType.SALAMANDER) -> {
                when (style) {
                    0 -> XpMode.STRENGTH
                    1 -> XpMode.RANGED
                    else -> XpMode.MAGIC
                }
            }

            else -> XpMode.ATTACK
        }
    }
}
