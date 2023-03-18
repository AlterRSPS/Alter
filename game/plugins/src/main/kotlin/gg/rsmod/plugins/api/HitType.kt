package gg.rsmod.plugins.api

import gg.rsmod.plugins.api.HitType.Companion.SplatColors.*

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
 */
enum class HitType(val id: Int) {
    POISON(id = GREEN.color), // NA to others, ONLY player themselves
    DISEASE(id = DEFORM_YELLOW.color), // NA to others, ONLY player themselves
    VENOM(id = TEAL.color), // NA to others, ONLY player themselves
    NPC_HEAL(id = MAGENTA.color), // generally used for NPCs -> NO player distinction
    BLOCK(id = 12), // blue -> blue
    BLOCK_W(id = 13), // blue -> dark blue
    HIT(id = 16), // red -> red
    HIT_W(id = 17), // red -> dark red
    CYAN(id = 18), // damage dealt to Verzik Vitur's and The Nightmare's shield by player
    CYAN_W(id = 19), // damage dealt to Verzik Vitur's and The Nightmare's shield by others
    ORANGE(id = 20), // Zalcano armor damage by player
    ORANGE_W(id = 21), // Zalcano armor damage by others
    YELLOW(id = 22), // charge "damage" dealt to nightmare totems by player
    YELLOW_W(id = 23), // charge "damage" dealt to nightmare totems by others
    GREY(id = 24), // parasite "healing" nightmare totems for player
    GREY_W(id = 25), // parasite "healing" nightmare totems for others
    TEST(id = 26),
    TEST2(id = 27),
    TEST3(id = 28),
    TEST4(id = 29),
    TEST5(id = 30),
    TEST6(id = 31),
    TEST7(id = 32),
    TEST8(id = 33),
    TEST9(id = 34),
    TEST10(id = 35),
    TEST11(id = 36),
    TEST12(id = 37),
    TEST13(id = 38),
    TEST14(id = 39),
    TEST15(id = 40),
    TEST16(id = 41),
    TEST17(id = 42),
    TEST18(id = 43),
    TEST19(id = 44),
    TEST20(id = 45),
    TEST21(id = 46),
    TEST22(id = 47),
    MAX_HIT(id = 48),
    TEST24(id = 49),
    TEST25(id = 50),
    TEST26(id = 51),
    TEST27(id = 52),
    TEST28(id = 53),
    TEST29(id = 54),
    TEST30(id = 55),
    TEST31(id = 56),
    TEST32(id = 57),
    TEST33(id = 58),
    TEST34(id = 59),
    TEST35(id = 60),
    TEST36(id = 61),
    TEST37(id = 62),
    TEST38(id = 63),
    TEST39(id = 64),
    TEST40(id = 65),
    TEST41(id = 66),



    TEST43(id = 68),
    TEST44(id = 69),
    TEST45(id = 70),
    TEST46(id = 71),
    TEST47(id = 72),
    TEST48(id = 73),
    TEST49(id = 74),
    TEST50(id = 75),
    TEST51(id = 76),
    TEST52(id = 77),
    TEST53(id = 78),
    TEST54(id = 79),
    TEST55(id = 80),
    TEST56(id = 81),
    TEST57(id = 82),
    TEST58(id = 83),
    TEST59(id = 84),
    TEST60(id = 85),
    TEST61(id = 86),
    TEST62(id = 87),
    TEST63(id = 88),
    TEST64(id = 89),
    TEST65(id = 90),
    TEST66(id = 91),
    TEST67(id = 92),
    TEST68(id = 93),
    TEST69(id = 94);


    companion object {
        val TINTED_HITMARK_VARBIT = 10236

        /**
         * these [SplatColors] can technically be used directly
         * to display the desired colored hitmark
         */
        enum class SplatColors(val color: Int){
            GREEN(2),
            DARK_YELLOW(3),
            DEFORM_YELLOW(4),
            TEAL(5), // venom
            MAGENTA(6), // NPC healing (primarily reserved for bosses)
            BLUE(26),
            DARK_BLUE(27),
            RED(28),
            DARK_RED(29),
            YELLOW(30),
            UGLY_YELLOW(31),
            GREY(32),
            DARK_GREY(33),
            CYAN(34),
            DARK_CYAN(35),
            ORANGE(36),
            DARK_ORANGE(37);
        }

        val values = values()

        fun get(id: Int): HitType? {
            values.forEach {
                if(it.id == id) return it
            }
            return null
        }
    }
}