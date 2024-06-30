package org.alter.api

private val SkillJingle =
    setOf(
        0 to SkillJingles(0, 29, 30), // ATTACK
        1 to SkillJingles(1, 37, 38), // DEFENCE
        2 to SkillJingles(2, 33, 34), // STRENGTH
        3 to SkillJingles(3, 47, 48), // HITPOINTS Jingle first Levels 11-49 || And Jingle Second Levels 50-99
        4 to SkillJingles(4, 57, 58), // RANGED
        5 to SkillJingles(5, 55, 56), // PRAYER
        6 to SkillJingles(6, 51, 52), // MAGIC
        7 to SkillJingles(7, 33, 34), // COOKING
        8 to SkillJingles(8, 69, 70), // WOODCUTTING
        9 to SkillJingles(9, 43, 44), // FLETCHING
        10 to SkillJingles(10, 41, 42), // FISHING
        11 to SkillJingles(11, 39, 40), // FIREMAKING
        12 to SkillJingles(12, 35, 36), // CRAFTING
        13 to SkillJingles(13, 63, 64), // SMITHING
        14 to SkillJingles(14, 53, 54), // MINING
        15 to SkillJingles(15, 45, 46), // HERBLORE
        16 to SkillJingles(16, 28), // AGILITY
        17 to SkillJingles(17, 67, 68), // THIEVING
        18 to SkillJingles(18, 61, 62), // SLAYER
        19 to SkillJingles(19, 10, 11), // FARMING
        20 to SkillJingles(20, 59, 60), // RUNECRAFTING
        21 to SkillJingles(21, 49, 50), // HUNTER Even-numbered levels  ||  Odd-numbered levels
        22 to SkillJingles(22, 31, 32), // CONSTRUCTION
    )

data class SkillJingles(var SkillID: Int, var JingleID: Int, var JingleID_Unlock: Int = -1)
