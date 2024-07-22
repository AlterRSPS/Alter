package org.alter.plugins.content

import org.alter.game.model.weightedTableBuilder.Loot
import org.alter.game.model.weightedTableBuilder.LootTable
import org.alter.game.model.weightedTableBuilder.TableType

val imp = Npcs.IMP_3134
val tile = Tile(3235, 3219, 0)


for (x in -2 until 2) {
    for (z in -2 until 2) {
        spawn_npc(imp, x = tile.x + x, z = tile.z + z)
    }
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

    species {
        +NpcSpecies.DEMON
        +NpcSpecies.DRACONIC
    }
    /**
     * @TODO
     * Add Combat Script => One more way to script the Npc Combat <-- Hmm but shiit We also need to add support for attack types
     * Aswell add a way to cancel player attacking blocked monsters example (Cyclops)
     *
     */
    stats {
        hitpoints = 10
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
    drops {
        always {
            add(Items.FIENDISH_ASHES, 1)
            add(test())
        }
        main(128) {
            add(Items.BLACK_BEAD, weight = 5)
            add(Items.RED_BEAD, weight = 5)
            add(Items.WHITE_BEAD, weight = 5)
            add(Items.YELLOW_BEAD, weight = 5)
            add(Items.BRONZE_BOLTS, weight = 8)
            add(Items.BLUE_WIZARD_HAT, weight = 8)
            add(Items.EGG, weight = 5)
            add(Items.RAW_CHICKEN, weight = 5)
            add(Items.BURNT_BREAD, weight = 4)
            add(Items.BURNT_MEAT, weight = 4)
            add(Items.CABBAGE, weight = 2)
            add(Items.BREAD_DOUGH, weight = 2)
            add(Items.BREAD, weight = 1)
            add(Items.COOKED_MEAT, weight = 1)
            add(Items.HAMMER, weight = 8)
            add(Items.TINDERBOX, weight = 5)
            add(Items.SHEARS, weight = 4)
            add(Items.BUCKET, weight = 4)
            add(Items.BUCKET_OF_WATER, weight = 2)
            add(Items.JUG, weight = 2)
            add(Items.JUG_OF_WATER, weight = 2)
            add(Items.POT, weight = 2)
            add(Items.POT_OF_FLOUR, weight = 2)
            add(Items.BALL_OF_WOOL, weight = 8)
            add(Items.MIND_TALISMAN, weight = 7)
            add(Items.ASHES, weight = 6)
            add(Items.CLAY, weight = 4)
            add(Items.CADAVA_BERRIES, weight = 4)
            add(Items.GRAIN, weight = 3)
            add(Items.CHEFS_HAT, weight = 2)
            add(Items.FLYER, weight = 2)
            add(Items.POTION, weight = 1)
        }
        tertiary(5000) {
            add(Items.ENSOULED_IMP_HEAD, 1, 25)
            add(Items.ECUMENICAL_KEY, 1, 60)
            add(Items.IMP_CHAMPION_SCROLL, 1, 5000)
        }
        /**
         * [13:17:28.097] ERROR GameService - Cycle took longer than expected: 654ms / 600ms!
         * [13:17:28.099] ERROR GameService - {class org.alter.game.task.SequentialSynchronizationTask=1, class org.alter.game.task.ChunkCreationTask=0, class org.alter.game.task.WorldRemoveTask=0, class org.alter.game.task.QueueHandlerTask=0, class org.alter.game.task.MessageHandlerTask=0, class org.alter.game.task.PlayerCycleTask=0, class org.alter.game.task.NpcCycleTask=0}
         * [13:17:28.100] ERROR GameService - {123=0}
         *
         *
         * @TODO Add code block for --> OnDrop()
         * And make so that we could create rollable table
         */
    }
}


fun test() : LootTable {
    val loot = mutableSetOf<Loot>()
    loot.add(Loot(Items.TWISTED_BOW, 1))
    return LootTable(
        tableType = TableType.ALWAYS,
        drops = loot
    )
}