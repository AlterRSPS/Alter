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
    immunities {
        poison = true
        venom = true
    }
   /* defence {
        melee {
            stab = -42
            slash = -42
            crush = -42
        }
        /**
        magic {
            magic = -42
            /**
             * @TODO As i seen on wiki , all weaknesses involve only single elements,
             * I guess we could just support Multiple
             */
            elementWeaknes = {
                /**
                 * Element.EARTH
                 * Element.AIR
                 * Element.WATER
                 * Element.FIRE
                 * Element.FIRE
                  */
                200
            }
        }
        */
        range {
            darts = -42
            arrows = -42
            bolts = -42
        }
    } */
    species {
        NpcSpecies.DEMON
    }
    /**
     * @TODO
     * Add Combat Script => One more way to script the Npc Combat <-- Hmm but shiit We also need to add support for attack types
     * Aswell add a way to cancel player attacking blocked monsters example (Cyclops)
     *
     */
    stats {
        hitpoints = 100
        defence = -100 // @TODO Rework and implement new defence system
    }

    bonuses {
        attackStab = -42
        strengthBonus = -37
        attackMagic = 0
        magicDamageBonus = 0
        attackRanged = 0
        rangedStrengthBonus = 0
    }


    /**
     * @TODO
     * New defence system, 3 DSL Builders
     * : Melee Defence , Magic Defence , Ranged defence
     *
     * Melee Defence:
     * Stab Weapons + 70 , Slash Weapons + 70 , Crush Weapons + 70
     *
     * Magic Defence:
     * Magic + 70 , (No elemental Weakness(Max Hit=⌊⌊Base Max×(1+Magic Damage%)⌋×(1+Slayer or Salve%)⌋+⌊Base Max×Elemental Weakness%⌋))
     *
     * Ranged Defence:
     * Darts + 70
     * Arrows + 70
     * Bolts + 70
     *
     */


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
