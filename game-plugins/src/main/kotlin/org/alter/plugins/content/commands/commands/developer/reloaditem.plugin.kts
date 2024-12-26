package org.alter.plugins.content.commands.commands.developer

import dev.openrune.cache.CacheManager.itemSize
import org.alter.game.model.priv.Privilege
import org.alter.game.service.game.ItemMetadataService

/**
 * @author CloudS3c
 */
on_command("reloaditems", Privilege.DEV_POWER, description = "Reload all itemdefs") {
    world.getService(ItemMetadataService::class.java)!!.loadAll()
    player.message("All items were reloaded. Defs: ${itemSize()} Took: ${world.getService(ItemMetadataService::class.java)!!.ms} ms.")
}
