package org.alter.plugins.content.npcs.barrows

spawnNpc("npc.karil_the_tainted", 3565, 3275, 0, 2)
spawnNpc("npc.karil_the_tainted", 3563, 3272, 0, 2)
spawnNpc("npc.karil_the_tainted", 3563, 3278, 0, 2)
spawnNpc("npc.karil_the_tainted", 3567, 3272, 0, 2)
spawnNpc("npc.karil_the_tainted", 3567, 3278, 0, 2)

setCombatDef("npc.karil_the_tainted") {
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
