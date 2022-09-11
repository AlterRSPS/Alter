package gg.rsmod.plugins.content.npcs.barrows

spawn_npc(Npcs.GUTHAN_THE_INFESTED, 3577, 3282, 0, 2);
spawn_npc(Npcs.GUTHAN_THE_INFESTED, 3579, 3279, 0, 2);
spawn_npc(Npcs.GUTHAN_THE_INFESTED, 3579, 3285, 0, 2);
spawn_npc(Npcs.GUTHAN_THE_INFESTED, 3575, 3279, 0, 2);
spawn_npc(Npcs.GUTHAN_THE_INFESTED, 3575, 3285, 0, 2);

set_combat_def(Npcs.GUTHAN_THE_INFESTED) {
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