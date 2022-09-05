package gg.rsmod.plugins.content.npcs.barrows

spawn_npc(Npcs.VERAC_THE_DEFILED, 3557, 3297, 0, 2);
spawn_npc(Npcs.VERAC_THE_DEFILED, 3559, 3300, 0, 2);
spawn_npc(Npcs.VERAC_THE_DEFILED, 3555, 3297, 0, 2);
spawn_npc(Npcs.VERAC_THE_DEFILED, 3555, 3294, 0, 2);
spawn_npc(Npcs.VERAC_THE_DEFILED, 3559, 3294, 0, 2);

set_combat_def(Npcs.VERAC_THE_DEFILED) {
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