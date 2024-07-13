package org.alter.plugins.content

import org.alter.api.cfg.Graphic


val imp = Npcs.IMP_3134
val tile = Tile(3235, 3220, 0)
for (i in 0 until 5) {
    spawn_npc(imp, x = tile.x + i, z = tile.z)
}

set_combat_def(imp) {
    configs {
        attackSpeed = 6
        respawnDelay = 40
    }

    stats {
        hitpoints = 100
        defence = -100 // @TODO Rework and implement new defence system
    }

    anims {
        attack = Animation.IMP_ATTACK
        block = Animation.IMP_DEFEND
        death = Animation.IMP_DEATH
    }
    sound {
        attackSound = Sound.IMP_ATTACK
        blockSound = Sound.IMP_HIT
        deathSound = Sound.IMP_DEATH
    }
}
val mm = Npcs.MANIACAL_MONKEY_6803
on_npc_death(imp) {
    world.spawn(Npc(mm, npc.tile, npc.world))
}
on_npc_spawn(mm) {
    /**
     * Hmm maybe would be nice to freeze spawn till this one dies
     */
    for (x in -3..3) {
        for (z in -3..3) {
            val gfx_tile = Tile(npc.tile.x + x, npc.tile.z + z, npc.tile.height)
            world.spawn(TileGraphic(tile = gfx_tile, id = Graphic.ALCHEMICAL_HYDRA_FIRE,99, 0))
        }
    }
}
set_combat_def(mm) {
    configs {
        attackSpeed = 6
        respawnDelay = 0
    }
    stats {
        hitpoints = 250
        defence = 99
    }
    anims {
        attack = Animation.MONKEY_ATTACK
        block = Animation.MONKEY_HIT
        death = Animation.MONKEY_ARCHER_DEATH
    }
    sound {
        attackSound = Sound.MONKEY_ATTACK
        blockSound = Sound.MONKEY_HIT
        deathSound = Sound.MONKEY_DEATH
    }
}
