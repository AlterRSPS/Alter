package gg.rsmod.plugins.content.mechanics.npcspawns

/**
 * @author CloudS3c
 * Just for testing npc :S
 */
// x: 2399 z: 9440 y: 0`
arrayOf(Npcs.MOLANISK_1).forEach { npc ->
    set_combat_def(npc) {
        configs {
            attackSpeed = 999
            respawnDelay = 5
        }
        anims {

            attack = 6011
            block = 6012
            death = 6014
        }
        stats {
            hitpoints = 9999
        }
    }
}


spawn_npc(Npcs.MOLANISK_1, 2399, 9440, 0)
load_service(NpcSpawnService())
