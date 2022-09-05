package gg.rsmod.plugins.content.npcs.barrows

spawn_npc(Npcs.TORAG_THE_CORRUPTED, 3552, 3283, 0, 2);
spawn_npc(Npcs.TORAG_THE_CORRUPTED, 3551, 3280, 0, 2);
spawn_npc(Npcs.TORAG_THE_CORRUPTED, 3551, 3285, 0, 2);
spawn_npc(Npcs.TORAG_THE_CORRUPTED, 3554, 3280, 0, 2);
spawn_npc(Npcs.TORAG_THE_CORRUPTED, 3556, 3284, 0, 2);

set_combat_def(Npcs.TORAG_THE_CORRUPTED) {
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