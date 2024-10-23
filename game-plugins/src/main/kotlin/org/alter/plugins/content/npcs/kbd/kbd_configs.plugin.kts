package org.alter.plugins.content.npcs.kbd

set_multi_combat_region(region = 9033)

spawn_npc(Npcs.KING_BLACK_DRAGON, x = 2274, y = 4698, walkRadius = 5)

set_combat_def(Npcs.KING_BLACK_DRAGON) {
    species {
        +NpcSpecies.DRACONIC
        +NpcSpecies.BASIC_DRAGON
    }

    configs {
        attackSpeed = 3
        respawnDelay = 50
    }

    aggro {
        radius = 16
        searchDelay = 1
    }

    stats {
        hitpoints = 240
        attack = 240
        strength = 240
        defence = 240
        magic = 240
    }

    bonuses {
        defenceStab = 70
        defenceSlash = 90
        defenceCrush = 90
        defenceMagic = 80
        defenceRanged = 70
    }

    anims {
        block = 89
        death = 92
    }

    slayerData {
        levelRequirement = 50
        xp = 258.0
    }

//    drops {
//        position = Tile(x = 3222, z = 3222)
//
//        always {
//            add(Items.DRAGON_BONES, 1)
//            add(Items.BLACK_DRAGON_LEATHER, 1)
//        }
//
//        main(tableWeight = 128) {
//            add(itemid = Items.RUNE_LONGSWORD, min = 1, weight = 10)
//            add(itemid = Items.ADAMANT_PLATEBODY, min = 1, weight = 9)
//            add(itemid = Items.ADAMANT_KITESHIELD, min = 1, weight = 3)
//            add(itemid = Items.DRAGON_MED_HELM, min = 1, weight = 1)
//            add(itemid = Items.FIRE_RUNE, min = 300, weight = 5)
//            add(itemid = Items.AIR_RUNE, min = 300, weight = 10)
//            add(itemid = Items.IRON_ARROW, min = 690, weight = 10)
//            add(itemid = Items.RUNITE_BOLTS, min = 10, weight = 10)
//            add(itemid = Items.LAW_RUNE, min = 30, weight = 5)
//            add(itemid = Items.BLOOD_RUNE, min = 30, weight = 5)
//            add(itemid = Items.YEW_LOGS_NOTED, min = 150, weight = 10)
//            add(itemid = Items.ADAMANTITE_BAR, min = 3, weight = 5)
//            add(itemid = Items.RUNITE_BAR, min = 1, weight = 3)
//            add(itemid = Items.GOLD_ORE_NOTED, min = 100, weight = 2)
//            add(itemid = Items.AMULET_OF_POWER, min = 1, weight = 7)
//            add(itemid = Items.DRAGON_ARROWTIPS, min = 5, weight = 5)
//            add(itemid = Items.DRAGON_DART_TIP, min = 5, weight = 5)
//            add(itemid = Items.DRAGON_JAVELIN_HEADS, min = 15, weight = 5)
//            add(itemid = Items.RUNITE_LIMBS, min = 1, weight = 4)
//            add(itemid = Items.SHARK, min = 4, weight = 4)
//        }
//    }
}
