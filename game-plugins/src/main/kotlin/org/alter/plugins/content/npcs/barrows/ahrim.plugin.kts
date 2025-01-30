package org.alter.plugins.content.npcs.barrows

spawnNpc("npc.ahrim_the_blighted", 3565, 3289, 0, 2)
spawnNpc("npc.ahrim_the_blighted", 3563, 3286, 0, 2)
spawnNpc("npc.ahrim_the_blighted", 3563, 3291, 0, 2)
spawnNpc("npc.ahrim_the_blighted", 3567, 3291, 0, 2)
spawnNpc("npc.ahrim_the_blighted", 3568, 3288, 0, 2)

setCombatDef("npc.ahrim_the_blighted") {
    configs {
        attackSpeed = 6
        respawnDelay = 50
    }

    stats {
        hitpoints = 100
        magic = 100
        defence = 100
    }

    anims {
        attack = 729
        block = 2079
        death = 2925
    }
}
