package gg.rsmod.plugins.content.npcs.barrows

spawn_npc(Npcs.KARIL_THE_TAINTED, 3565, 3275, 0, 2);
spawn_npc(Npcs.KARIL_THE_TAINTED, 3563, 3272, 0, 2);
spawn_npc(Npcs.KARIL_THE_TAINTED, 3563, 3278, 0, 2);
spawn_npc(Npcs.KARIL_THE_TAINTED, 3567, 3272, 0, 2);
spawn_npc(Npcs.KARIL_THE_TAINTED, 3567, 3278, 0, 2);

set_combat_def(Npcs.KARIL_THE_TAINTED) {
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