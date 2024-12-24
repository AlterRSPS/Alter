/**
 * @author CloudS3c 11/17/2024
 */
val test_size_npc = Npcs.GENERAL_GRAARDOR
val test_size_tile = Tile(3159,3471,0)
spawn_npc(test_size_npc, test_size_tile)
set_combat_def(test_size_npc) {
    configs {
        attackSpeed = 2
        respawnDelay = 10
    }
    defence {
        melee {
            stab = -42
            slash = -42
            crush = -42
        }
        magic {
            magic = -42
            elementWeakness = ElementalWeakness(Elements.AIR, 200)
        }
        range {
            darts = -42
            arrows = -42
            bolts = -42
        }
    }
    stats {
        hitpoints = 999
        defence = -100
    }
    bonuses {
        attackStab = -999
        strengthBonus = -999
        attackMagic = -999
        magicDamageBonus = 0
        attackRanged = 0
        rangedStrengthBonus = 0
    }
    anims {
        attack = Animation.GENERAL_GRAARDOR_MELEE_ATTACK
        block = Animation.GENERAL_GRAARDOR_DEFEND
        death = Animation.GENERAL_GRAARDOR_DEATH
    }
    sound {
        attackSound = Sound.IMP_ATTACK
        blockSound = Sound.IMP_HIT
        deathSound = Sound.IMP_DEATH
    }
}