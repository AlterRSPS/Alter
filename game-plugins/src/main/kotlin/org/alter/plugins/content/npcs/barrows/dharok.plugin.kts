package org.alter.plugins.content.npcs.barrows

spawnNpc("npc.dharok_the_wretched", 3576, 3298, 0, 2)
spawnNpc("npc.dharok_the_wretched", 3576, 3300, 0, 2)
spawnNpc("npc.dharok_the_wretched", 3573, 3299, 0, 2)
spawnNpc("npc.dharok_the_wretched", 3578, 3296, 0, 2)
spawnNpc("npc.dharok_the_wretched", 3574, 3295, 0, 2)

setCombatDef("npc.dharok_the_wretched") {
    configs {
        attackSpeed = 7
        respawnDelay = 50
    }

    stats {
        hitpoints = 100
        attack = 100
        strength = 100
        defence = 100
    }

    anims {
        attack = 2067
        block = 424
        death = 2925
    }
}
