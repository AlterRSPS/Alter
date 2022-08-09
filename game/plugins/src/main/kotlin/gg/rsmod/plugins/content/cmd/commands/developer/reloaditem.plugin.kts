package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.service.game.ItemMetadataService

/**
 * @author CloudS3c
 * Reloads items definition by it's id. Item has to be equippable. And it's id stored in data/cfg/items/equipables
 */
on_command("reloaditems", Privilege.DEV_POWER, description = "Reload all itemsdefs") {
    world.getService(ItemMetadataService::class.java)!!.loadAll(world)
    player.message("All items were reloaded. Defs: ${world.definitions.getCount(ItemDef::class.java)} Took: ${world.getService(ItemMetadataService::class.java)!!.ms} ms.")
}