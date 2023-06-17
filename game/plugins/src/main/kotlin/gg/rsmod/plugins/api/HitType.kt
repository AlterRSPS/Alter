package gg.rsmod.plugins.api

/**
 * @author bmyte
 * added support for tinted hitmarks
 *
 * ordinals suffixed with `_W` are reserved for player distinct operations in world not involving
 * the player themselves (i.e. hits generated/taken by entities other than the player) and corresponds
 * to the non suffixed ordinal; which is reserved for the player distinct operations in world by the player.
 *   Note| while the non-player relevant operations are observed to be a single increment offset from
 *   the base [HitType], this might not always be and as such a separate [HitType] ordinal is reserved
 *
 * implementation sees that the base [HitType] is used for registering hits
 * but that the appropriate suffixed ordinal [id] is sent during updates
 * depending on player relevance -> the client handles tinted hitmark displaying
 * based on [TINTED_HITMARK_VARBIT] current state
 * _T Indicates tinted. => Not approved but guess tinted versions are used on mass bosses.
 * And tinted versions shows dealt damage by others not yourself.
 */
enum class HitType(val id: Int) {
    /**
     * Corruption has a chance to apply on the target of the player who is under the effects of either Lesser Corruption or Greater Corruption during a successful hit.
     * Corruption which drains prayer points over a short period of time.
     */
    CORRUPTION(id = 0),

    /**
     * Poison hitsplat.png 	Poison, which damages players at set intervals, and decreases over time.
     * Can also indicate eating a poison karambwan as well as being harmed by Poison Gas. Green eggs fired at enemies in Barbarian Assault also show this.
     * Poison damages entities over time, lowering the damage by one every four hit splat cycles.
     */
    POISON(id = 2),

    /**
     * The use case of this hit splat is currently unknown. While it resembles disease rather closely, it is not used for disease in OldSchool RuneScape.
     */
    DISEASE(id = 3),

    /**
     * Disease, which drains a player's stats at set intervals, excluding Hitpoints and Prayer.
     * Indicates the player being under the effects of a disease, which periodically drains stats.
     */
    DISEASE_DRAIN(id = 4),

    /**
     * Venom damages entities over time, increasing the damage by one every four hit splat cycles, capping out at 20.
     */
    VENOM(id = 6),

    /**
     * Used to represent an NPC healing its hitpoints, though it is mostly reserved for boss encounters.
     */
    NPC_HEAL(id = 6), // generally used for NPCs -> NO player distinction

    /**
     * Indicates a hit of zero damage.
     */
    BLOCK(id = 12),
    BLOCK_T(id = 13),
    BLOCK_U(id = 26), // Unknown
    BLOCK_U_T(id = 27), // Unknown

    /**
     * Indicates a successful hit that dealt damage. In the Nightmare Zone,
     * drinking an absorption potion will cause all monster-inflicted damage to be zero,
     * but will still use this red damage hit splat to indicate the successful hit.
     */
    HIT(id = 16),
    HIT_T(id = 17),
    HIT_U(id = 28),
    HIT_U_T(id = 29),
    HIT_MAX(id = 43),
    HIT_MAX_U(id = 48),

    /**
     * Indicates damage dealt to Verzik Vitur’s, The Nightmare’s and Tempoross’ shields.
     * While the inactive icon is defined in the config, it seems to be a placeholder.
     */
    SHIELD(id = 18),
    SHIELD_T(id = 19),
    SHIELD_U(id = 34),
    SHIELD_U_T(id = 35),
    SHIELD_MAX(id = 44),
    SHIELD_MAX_U(id = 51),

    /**
     * Indicates damage dealt to Zalcano’s stone armour. The dynamic id is never actually used.
     * Armour Shown when damaging Zalcano's stone armour.
     */
    ARMOUR(id = 20),
    ARMOUR_T(id = 21),
    ARMOUR_U(id = 36),
    ARMOUR_U_T(id = 37),
    ARMOUR_MAX(id = 45),
    ARMOUR_MAX_U(id = 52),

    /**
     * Indicates totems being healed while charging them during the fight against The Nightmare.
     * Shown when The Nightmare's totems are charged or "healed".
     */
    CHARGE(id = 22), // charge "damage" dealt to nightmare totems by player
    CHARGE_T(id = 23), // charge "damage" dealt to nightmare totems by others
    CHARGE_U(id = 30),
    CHARGE_U_T(id = 31),
    CHARGE_MAX(id = 46),
    CHARGE_MAX_U(id = 49),

    /**
     * Indicates totems being damaged while the parasites discharge them during the fight against The Nightmare.
     * The dynamic is never actually used.
     * Shown when The Nightmare's totems are uncharged or "damaged" by parasites.
     */
    UNCHARGE(id = 24), // parasite "healing" nightmare totems for player
    UNCHARGE_T(id = 25), // parasite "healing" nightmare totems for others
    UNCHARGE_U(id = 32),
    UNCHARGE_U_T(id = 33),
    UNCHARGE_MAX(id = 47),
    UNCHARGE_MAX_U(id = 50),

    /**
     * Dodged damage from a negated non-typeless attack. Currently unused.
     */
    POISE(id = 53),
    POISE_T(id = 38),
    POISE_T_U(id = 54),
    POSE_MAX(id = 55),
    POISE_U(id = 56),
    POISE_T_U_2(id = 57),
    POISE_T_U_MAX(id = 58),

    /**
     * Alternate charge hitsplat representing the growth of the Palm of Resourcefulness,
     * as well as the restoration of Kephri's scarab shield within the Tombs of Amascut raid.
     */
    ALT_CHARGE(id = 39),
    ALT_CHARGE_T(id = 40),

    /**
     * Alternate uncharge hitsplat representing the crocodile's damage towards the Palm of Resourcefulness.
     */
    ALT_UNCHARGE(id = 41),
    ALT_UNCHARGE_T(id = 42),

    /**
     * Shown when draining the Phantom Muspah's prayer shield. Currently, the max hit variant is unused.
     */
    PRAYER_DRAIN(id = 59),
    PRAYER_DRAIN_T(id = 60),
    PRAYER_DRAIN_MAX(id = 61),
    PRAYER_DRAIN_U(id = 62),
    PRAYER_DRAIN_U_T(id = 63),
    PRAYER_DRAIN_U_MAX(id = 64);


    companion object {
        val TINTED_HITMARK_VARBIT = 10236
        val values = values()
        fun get(id: Int): HitType? {
            values.forEach {
                if(it.id == id) return it
            }
            return null
        }
    }
}