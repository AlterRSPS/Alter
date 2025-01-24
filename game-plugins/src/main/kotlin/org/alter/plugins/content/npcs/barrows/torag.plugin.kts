package org.alter.plugins.content.npcs.barrows

spawnNpc("npc.torag_the_corrupted", 3552, 3283, 0, 2)
spawnNpc("npc.torag_the_corrupted", 3551, 3280, 0, 2)
spawnNpc("npc.torag_the_corrupted", 3551, 3285, 0, 2)
spawnNpc("npc.torag_the_corrupted", 3554, 3280, 0, 2)
spawnNpc("npc.torag_the_corrupted", 3556, 3284, 0, 2)

setCombatDef("npc.torag_the_corrupted") {
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
