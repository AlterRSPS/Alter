package org.apollo.plugins.drops.impl
/*
import org.apollo.plugin.api.entity.mob.npc.plugin.NpcPluginBuilder
import org.apollo.utility.constants.generated.ItemTypeConstants
import org.apollo.utility.constants.generated.NpcTypeConstants
import org.apollo.plugin.api.noted
import org.apollo.plugin.api.table.DropTable

// if [[monkey madness ii]] has not been completed, the drop rate of fire runes are 10/128.
// dragon javelin heads are only dropped after completion of [[monkey madness ii]].
// https://oldschool.runescape.wiki/w/King_Black_Dragon
//LOGIC ERROR: Our total weights (126) are less than the denominator(128), perhaps missing nothing(2) call. Grouping is {=weapons and armour==23, =runes and ammunition==45, =resources==20, =other==30, =rare and gem drop table==8, =deadman mode==2}
npc(NpcTypeConstants.KING_BLACK_DRAGON) {
    // https://oldschool.runescape.wiki/w/King_Black_Dragon
    //LOGIC ERROR: Our total weights (126) are less than the denominator(128), perhaps missing nothing(2) call. Grouping is {=weapons and armour==23, =runes and ammunition==45, =resources==20, =other==30, =rare and gem drop table==8, =deadman mode==2}
    drops {
        always {
            add(id = ItemTypeConstants.DRAGON_BONES, minAmount = 1, maxAmount = 1)
            add(id = ItemTypeConstants.BLACK_DRAGONHIDE, minAmount = 2, maxAmount = 2)
        }
        preroll {
            add(id = ItemTypeConstants.DRAGON_PICKAXE, minAmount = 1, maxAmount = 1, numerator=1, denominator=1500)
            add(id = ItemTypeConstants.DRAGON_PICKAXE, minAmount = 1, maxAmount = 1, numerator=1, denominator=750)
            add(id = ItemTypeConstants.DRACONIC_VISAGE, minAmount = 1, maxAmount = 1, numerator=1, denominator=1250)
        }
        main(total = 128) {
            add(weight = 8, block = DropTable.rareDropCondition(1))
            add(weight = 2, block = DropTable.gemDropCondition(rolls = 1))
            add(id = ItemTypeConstants.RUNE_LONGSWORD, minAmount = 1, maxAmount = 1, weight=10)
            add(id = ItemTypeConstants.ADAMANT_PLATEBODY, minAmount = 1, maxAmount = 1, weight=9)
            add(id = ItemTypeConstants.ADAMANT_KITESHIELD, minAmount = 1, maxAmount = 1, weight=3)
            add(id = ItemTypeConstants.DRAGON_MED_HELM, minAmount = 1, maxAmount = 1, weight=1)
            add(id = ItemTypeConstants.FIRE_RUNE, minAmount = 300, maxAmount = 300, weight=5)// if [[monkey madness ii]] has not been completed, the drop rate of fire runes are 10/128.
            add(id = ItemTypeConstants.AIR_RUNE, minAmount = 300, maxAmount = 300, weight=10)
            add(id = ItemTypeConstants.IRON_ARROW, minAmount = 690, maxAmount = 690, weight=10)
            add(id = ItemTypeConstants.RUNITE_BOLTS, minAmount = 10, maxAmount = 20, weight=10)
            add(id = ItemTypeConstants.LAW_RUNE, minAmount = 30, maxAmount = 30, weight=5)
            add(id = ItemTypeConstants.BLOOD_RUNE, minAmount = 30, maxAmount = 30, weight=5)
            add(id = ItemTypeConstants.YEW_LOGS.noted, minAmount = 150, maxAmount = 150, weight=10)
            add(id = ItemTypeConstants.ADAMANTITE_BAR, minAmount = 3, maxAmount = 3, weight=5)
            add(id = ItemTypeConstants.RUNITE_BAR, minAmount = 1, maxAmount = 1, weight=3)
            add(id = ItemTypeConstants.GOLD_ORE.noted, minAmount = 100, maxAmount = 100, weight=2)
            add(id = ItemTypeConstants.AMULET_OF_POWER, minAmount = 1, maxAmount = 1, weight=7)
            add(id = ItemTypeConstants.DRAGON_ARROWTIPS, minAmount = 5, maxAmount = 14, weight=5)
            add(id = ItemTypeConstants.DRAGON_DART_TIP, minAmount = 5, maxAmount = 14, weight=5)
            add(id = ItemTypeConstants.DRAGON_JAVELIN_HEADS, minAmount = 15, maxAmount = 15, weight=5)// dragon javelin heads are only dropped after completion of [[monkey madness ii]].
            add(id = ItemTypeConstants.RUNITE_LIMBS, minAmount = 1, maxAmount = 1, weight=4)
            add(id = ItemTypeConstants.SHARK, minAmount = 4, maxAmount = 4, weight=4)
        }
        tertiary {
            add(id = ItemTypeConstants.BRIMSTONE_KEY, minAmount = 1, maxAmount = 1,numerator=1, denominator=65, description="brimstone keys are only dropped while on a slayer task given by konar quo maten.") { player.konar(npc) }
            add(id = ItemTypeConstants.KBD_HEADS, minAmount = 1, maxAmount = 1, numerator=1, denominator=128)
            add(id = ItemTypeConstants.SCROLL_BOX_ELITE, minAmount = 1, maxAmount = 1, numerator=1, denominator = 450)
            add(id = ItemTypeConstants.PRINCE_BLACK_DRAGON, minAmount = 1, maxAmount = 1, numerator=1, denominator=3000)
            add(id = ItemTypeConstants.DRACONIC_VISAGE, minAmount = 1, maxAmount = 1, numerator=1, denominator=5000)
            add(id = ItemTypeConstants.UNIDENTIFIED_FRAGMENT_COMBAT, minAmount = 1, maxAmount = 1, numerator=1, denominator=70)
        }
    }
}
*/