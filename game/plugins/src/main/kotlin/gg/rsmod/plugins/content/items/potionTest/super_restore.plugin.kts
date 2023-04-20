
/**
 *   @Author Cl0ud
 */
/**
 * -->
 */
// player.inventory.equipment.getBonues()

val restoreList = intArrayOf(Items.SUPER_RESTORE1, Items.SUPER_RESTORE2, Items.SUPER_RESTORE3, Items.SUPER_RESTORE4)

restoreList.forEach {
    on_item_option(it, "Drink") {
        player.playSound(Sounds.DRINKING_POTION)
        /**
         * @TODO Remove debuffs
         * @TODO -> All skill levels get restored.
         */
        for (x in 0..player.equipmentBonuses.size) {
            player.message("Bonus: ${player.equipmentBonuses.get(x)}.")
        }
    }
}


onItemOnNpc(Items.SUPER_RESTORE1, Npcs.MANDY) {
    player.getInteractingNpc()
}
// Movement type
