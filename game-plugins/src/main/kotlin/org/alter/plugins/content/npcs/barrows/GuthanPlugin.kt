package org.alter.plugins.content.npcs.barrows

spawnNpc("npc.guthan_the_infested", 3577, 3282, 0, 2)
spawnNpc("npc.guthan_the_infested", 3579, 3279, 0, 2)
spawnNpc("npc.guthan_the_infested", 3579, 3285, 0, 2)
spawnNpc("npc.guthan_the_infested", 3575, 3279, 0, 2)
spawnNpc("npc.guthan_the_infested", 3575, 3285, 0, 2)

setCombatDef("npc.guthan_the_infested") {
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
