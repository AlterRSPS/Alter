package gg.rsmod.plugins.content.commands.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.service.game.ItemMetadataService

/**
 * @author CloudS3c
 */
on_command("reloaditems", Privilege.DEV_POWER, description = "Reload all itemdefs") {
    world.getService(ItemMetadataService::class.java)!!.loadAll(world)
    player.message("All items were reloaded. Defs: ${world.definitions.getCount(ItemDef::class.java)} Took: ${world.getService(ItemMetadataService::class.java)!!.ms} ms.")
}