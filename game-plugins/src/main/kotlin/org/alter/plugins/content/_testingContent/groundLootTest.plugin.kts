import org.alter.game.model.move.hasMoveDestination

/**
 * @author CloudS3c 11/3/2024
 */

/**
 * @TODO
 * Test purpose:
 * Item on Ground item,
 * Item on Npc,
 * Item on Object,
 *
 * Execute script 1 tile before.
 */

spawn_item(Items.LOGS,1, 3160, 3483)
spawn_item(Items.LOGS,5, 3161, 3483)
spawn_item(Items.STEEL_ARROW,200, 3164, 3481, 0, 10)
spawn_item(Items.STEEL_ARROW,200, 3165, 3481, 0, 10)

on_item_on_item(Items.TINDERBOX, Items.LOGS) {
    player.message("Working 1.")
}

on_item_on_ground_item(Items.TINDERBOX, Items.LOGS) {
    player.message("Working 2.")
}

/**
 * Executes way before reaching tile.
 */
on_ground_item_option(Items.LOGS, "Light") {
    while(player.hasMoveDestination()) {
        world.spawn(GroundItem(Items.TWISTED_BOW, 1,player.tile))
    }
    player.message("Seems to be working?")
}
spawn_obj(Objs.BRIMSTONE_CHEST, 3163, 3486, 0)
on_item_on_obj(Objs.BRIMSTONE_CHEST, Items.BRIMSTONE_KEY) {
    player.message("Working brim")
}


on_obj_option(Objs.BRIMSTONE_CHEST, 1) {
    player.message("Working brim option 1")
}

