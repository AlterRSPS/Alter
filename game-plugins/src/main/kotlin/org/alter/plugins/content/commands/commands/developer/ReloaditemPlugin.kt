package org.alter.plugins.content.commands.commands.developer

import dev.openrune.cache.CacheManager.itemSize
import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.game.service.game.ItemMetadataService

/**
 * @author CloudS3c
 */
class ReloaditemPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("reloaditems", Privilege.DEV_POWER, description = "Reload all itemdefs") {
            world.getService(ItemMetadataService::class.java)!!.loadAll()
            player.message("All items were reloaded. Defs: ${itemSize()} Took: ${world.getService(ItemMetadataService::class.java)!!.ms} ms.")
        }

    }
}
