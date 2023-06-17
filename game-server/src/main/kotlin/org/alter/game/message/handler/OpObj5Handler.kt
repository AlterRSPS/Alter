package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpObj5Message
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Player
import org.alter.game.model.priv.Privilege
import org.alter.game.model.attr.INTERACTING_GROUNDITEM_ATTR
import org.alter.game.model.entity.GroundItem
import org.alter.game.action.GroundItemPathAction

import java.lang.ref.WeakReference

class OpObj5Handler : MessageHandler<OpObj5Message> {

    override fun handle(client: Client, world: World, message: OpObj5Message) {
        /*
         * If tile is too far away, don't process it.
         */
        val tile = Tile(message.x, message.z, client.tile.height)
        if (!tile.viewableFrom(client.tile, Player.TILE_VIEW_DISTANCE)) {
            return
        }

        if (!client.lock.canGroundItemInteract()) {
            return
        }

        log(client, "Ground Item action 5: item=%d, x=%d, z=%d, movement=%d", message.id, message.x, message.z, message.movementType)

        /*
         * Get the region chunk that the object would belong to.
         */
        val chunk = world.chunks.getOrCreate(tile)
        val item = chunk.getEntities<GroundItem>(tile, EntityType.GROUND_ITEM).firstOrNull { it.item == message.id && it.canBeViewedBy(client) } ?: return

        if (message.movementType == 1 && world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(item.tile)
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_OPT_ATTR] = 5
        client.attr[INTERACTING_GROUNDITEM_ATTR] = WeakReference(item)
        client.executePlugin(GroundItemPathAction.walkPlugin)
    }
}