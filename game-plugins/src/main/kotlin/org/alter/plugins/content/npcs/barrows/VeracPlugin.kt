package org.alter.plugins.content.npcs.barrows

spawnNpc("npc.verac_the_defiled", 3557, 3297, 0, 2)
spawnNpc("npc.verac_the_defiled", 3559, 3300, 0, 2)
spawnNpc("npc.verac_the_defiled", 3555, 3297, 0, 2)
spawnNpc("npc.verac_the_defiled", 3555, 3294, 0, 2)
spawnNpc("npc.verac_the_defiled", 3559, 3294, 0, 2)

setCombatDef("npc.verac_the_defiled") {
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
