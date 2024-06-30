@file:Suppress("ktlint:standard:class-naming")

package org.alter.plugins.content.skills.mining

sealed class OreType(val level: Int, val xp: Double, val ore: Int, val respawnTime: IntRange) {
    // motherload mine

    // amathyst
    // TODO: Need to add Rune Essence Mine area with portal support
    // TODO: Need to add teleports into Rune Essence Mine
    object RUNE_ESSENCE : OreType(level = 1, xp = 5.0, ore = 1436, respawnTime = 0..0)

    object CLAY : OreType(level = 1, xp = 5.0, ore = 434, respawnTime = 2..3)

    object COPPER : OreType(level = 1, xp = 17.5, ore = 436, respawnTime = 4..6)

    object TIN : OreType(level = 1, xp = 17.5, ore = 438, respawnTime = 4..6)

    object BLURITE : OreType(level = 10, xp = 17.5, ore = 668, respawnTime = 25..30)

    object LIMESTONE : OreType(level = 10, xp = 26.5, ore = 3211, respawnTime = 0..0) // WIP

    object IRON : OreType(level = 15, xp = 35.0, ore = 440, respawnTime = 6..9)

    object SILVER : OreType(level = 20, xp = 40.0, ore = 442, respawnTime = 55..60)

    // volcanic ashe
    object COAL : OreType(level = 30, xp = 50.0, ore = 453, respawnTime = 25..30)

    object PURE_ESSENCE : OreType(level = 30, xp = 5.0, ore = 7936, respawnTime = 0..0)

    object SANDSTONE1 : OreType(level = 35, xp = 30.0, ore = 6971, respawnTime = 6..8)

    object SANDSTONE2 : OreType(level = 35, xp = 40.0, ore = 6973, respawnTime = 6..8)

    object SANDSTONE3 : OreType(level = 35, xp = 50.0, ore = 6975, respawnTime = 6..8)

    object SANDSTONE4 : OreType(level = 35, xp = 60.0, ore = 6977, respawnTime = 6..8)

    // dense essence
    object GOLD : OreType(level = 40, xp = 65.0, ore = 444, respawnTime = 55..65)

    // gem rocks
    object GEMSTONE1 : OreType(level = 40, xp = 65.0, ore = 1625, respawnTime = 100..105)

    object GEMSTONE2 : OreType(level = 40, xp = 65.0, ore = 1627, respawnTime = 100..105)

    object GEMSTONE3 : OreType(level = 40, xp = 65.0, ore = 1629, respawnTime = 100..105)

    object GEMSTONE4 : OreType(level = 40, xp = 65.0, ore = 1623, respawnTime = 100..105)

    object GEMSTONE5 : OreType(level = 40, xp = 65.0, ore = 1621, respawnTime = 100..105)

    object GEMSTONE6 : OreType(level = 40, xp = 65.0, ore = 1619, respawnTime = 100..105)

    object GEMSTONE7 : OreType(level = 40, xp = 65.0, ore = 1617, respawnTime = 100..105)

    // volcanic sulpher
    // lovakengj blast mine
    object GRANITE1 : OreType(level = 45, xp = 50.0, ore = 6979, respawnTime = 6..8)

    object GRANITE2 : OreType(level = 45, xp = 60.0, ore = 6981, respawnTime = 6..8)

    object GRANITE3 : OreType(level = 45, xp = 75.0, ore = 6983, respawnTime = 6..8)

    // volcanic mine
    object MITHRIL : OreType(level = 55, xp = 80.0, ore = 447, respawnTime = 120..150)

    object LOVAKITE : OreType(level = 65, xp = 10.0, ore = 13356, respawnTime = 30..45)

    object ADAMANTITE : OreType(level = 70, xp = 95.0, ore = 449, respawnTime = 240..260)

    // motherload mine upper level
    // We need to see about these SALTs, we are replacing the node with a blank node (vein)
    object TE_SALT : OreType(level = 72, xp = 5.0, ore = 22593, respawnTime = 5..10)

    object EFH_SALT : OreType(level = 72, xp = 5.0, ore = 25595, respawnTime = 5..10)

    object URT_SALT : OreType(level = 72, xp = 5.0, ore = 25597, respawnTime = 5..10)

    // basalt
    object RUNITE : OreType(level = 85, xp = 125.0, ore = 451, respawnTime = 720..750)
}
