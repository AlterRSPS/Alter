package org.alter.plugins.content.interfaces.emotes

import org.alter.api.cfg.Varbit

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class Emote(
    val slot: Int,
    val anim: Int,
    val gfx: Int = -1,
    val varbit: Int = -1,
    val requiredVarbitValue: Int = 1,
    val unlockDescription: String? = null,
) {
    YES(slot = 0, anim = 855),
    NO(slot = 1, anim = 856),
    BOW(slot = 2, anim = 858),
    ANGRY(slot = 3, anim = 859),
    THINK(slot = 4, anim = 857),
    WAVE(slot = 5, anim = 863),
    SHRUG(slot = 6, anim = 2113),
    CHEER(slot = 7, anim = 862),
    BECKON(slot = 8, anim = 864),
    LAUGH(slot = 9, anim = 861),
    JUMP_FOR_JOY(slot = 10, anim = 2109),
    YAWN(slot = 11, anim = 2111),
    DANCE(slot = 12, anim = 866),
    JIG(slot = 13, anim = 2106),
    SPIN(slot = 14, anim = 2107),
    HEADBANG(slot = 15, anim = 2108),
    CRY(slot = 16, anim = 860),
    BLOW_KISS(slot = 17, anim = 1374, gfx = 574),
    PANIC(slot = 18, anim = 2105),
    RASPBERRY(slot = 19, anim = 2110),
    CLAP(slot = 20, anim = 865),
    SALUTE(slot = 21, anim = 2112),
    GOBLIN_BOW(slot = 22, anim = 2127, varbit = Varbit.GOBLIN_EMOTES_VARBIT, requiredVarbitValue = 7),
    GOBLIN_SALUTE(slot = 23, anim = 2128, varbit = Varbit.GOBLIN_EMOTES_VARBIT, requiredVarbitValue = 7),
    GLASS_BOX(slot = 24, anim = 1131, varbit = Varbit.GLASS_BOX_EMOTE_VARBIT),
    CLIMB_ROPE(slot = 25, anim = 1130, varbit = Varbit.CLIMB_ROPE_EMOTE_VARBIT),
    LEAN(slot = 26, anim = 1129, varbit = Varbit.LEAN_EMOTE_VARBIT),
    GLASS_WALL(slot = 27, anim = 1128, varbit = Varbit.GLASS_WALL_EMOTE_VARBIT),
    IDEA(slot = 28, anim = 4276, gfx = 712, varbit = Varbit.IDEA_EMOTE_VARBIT),
    STAMP(slot = 29, anim = 1745, varbit = Varbit.STAMP_EMOTE_VARBIT),
    FLAP(slot = 30, anim = 4280, varbit = Varbit.FLAP_EMOTE_VARBIT),
    SLAP_HEAD(slot = 31, anim = 4275, varbit = Varbit.SLAP_HEAD_EMOTE_VARBIT),
    ZOMBIE_WALK(slot = 32, anim = 3544, varbit = Varbit.ZOMBIE_WALK_EMOTE_VARBIT),
    ZOMBIE_DANCE(slot = 33, anim = 3543, varbit = Varbit.ZOMBIE_DANCE_EMOTE_VARBIT),
    SCARED(slot = 34, anim = 2836, varbit = Varbit.SCARED_EMOTE_VARBIT),
    RABBIT_HOP(slot = 35, anim = 6111, varbit = Varbit.RABBIT_HOP_EMOTE_VARBIT), //
    SIT_UP(slot = 36, anim = 2763, varbit = Varbit.EXERCISE_EMOTES),
    PUSH_UP(slot = 37, anim = 2762, varbit = Varbit.EXERCISE_EMOTES),
    STAR_JUMP(slot = 38, anim = 2761, varbit = Varbit.EXERCISE_EMOTES),
    JOG(slot = 39, anim = 2764, varbit = Varbit.EXERCISE_EMOTES),
    FLEX(slot = 40, anim = 8917, gfx = -1, varbit = Varbit.FLEX_EMOTE_VARBIT),
    ZOMBIE_HAND(slot = 41, anim = 1708, gfx = 320, varbit = Varbit.ZOMBIE_HAND_EMOTE_VARBIT),
    HYPERMOBILE_DRINKER(slot = 42, anim = 7131, varbit = Varbit.HYPERMOBILE_DRINKER_EMOTE_VARBIT),
    SKILLCAPE(slot = 43, anim = -1), // @TODO
    AIR_GUITAR(slot = 44, anim = 4751, gfx = 1239, varbit = Varbit.AIR_GUITAR_EMOTE_VARBIT),
    URI_TRANSFORM(slot = 45, anim = -1, gfx = -1, varbit = Varbit.URI_TRANSFORM_EMOTE_VARBIT), // @TODO
    SMOOTH_DANCE(slot = 46, anim = 7533, varbit = Varbit.SMOOTH_DANCE_EMOTE_VARBIT),
    CRAZY_DANCE(slot = 47, anim = 7536, varbit = Varbit.CRAZY_DANCE_EMOTE_VARBIT),

    // bronze, silver, and gold shield, referencing the 3, 6, and 12 month packages from the Premier Club.
    PREMIER_SHIELD(slot = 48, anim = 7751, gfx = 1412, varbit = Varbit.PREMIER_SHIELD_EMOTE_VARBIT),
    EXPLORE(slot = 49, anim = 8541, varbit = Varbit.EXPLORE_VARBIT), // @TODO

    // Twisted gfx: 1749 | TrailBlazer gfx: 1835
    // Varbit: 11757 -> 3 for Twisted, 6 for trailblazer, 9 for shattered
    RELIC_UNLOCKED(slot = 50, anim = 8524, gfx = 1835, varbit = Varbit.RELIC_UNLOCKED_EMOTE_VARBIT, requiredVarbitValue = 9), // @TODO unlockDescription = "You can't use that emote unless you have stored a tier 3 relichunter \n outfit on the outfitstand in your player owned house League Hall."
    PARTY(slot = 51, anim = 10031, gfx = 2365, varbit = Varbit.PARTY_EMOTE_VARBIT),
    ;

    companion object {
        val values = enumValues<Emote>()
    }
}
