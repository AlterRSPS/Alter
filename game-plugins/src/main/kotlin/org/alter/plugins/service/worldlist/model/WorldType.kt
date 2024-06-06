package org.alter.plugins.service.worldlist.model

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the various types of game worlds. Courtesy of RuneLite
 * [https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/WorldType.java]
 */
enum class WorldType(val mask: Int) {
    FREE(0),
    MEMBERS(1),
    PVP(1 shl 2),
    BOUNTY(1 shl 5),
    SKILL_TOTAL(1 shl 7),
    QUEST_SPEEDRUNNING(1 shl 8),
    PVP_HIGH_RISK(1 shl 10),
    LAST_MAN_STANDING(1 shl 14),
    NOSAVE_MODE(1 shl 25),
    DEADMAN_TOURNAMENT(1 shl 26),
    FRESH_START_WORLD(1 shl 27),
    DEADMAN(1 shl 29),
    SEASONAL_DEADMAN(1 shl 30),

    // Turns out order does not matter.
    T1(0 shl 29),
    T2(1 shl 30),
}
