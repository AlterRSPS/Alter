package org.alter.game.message.handler

import org.alter.game.action.GroundItemPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpObjUMessage
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.attr.*
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Player
import org.alter.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpObjUHandler : MessageHandler<OpObjUMessage> {

    override fun handle(client: Client, world: World, message: OpObjUMessage) {
        /*
         * If tile is too far away, don't process it.
         */
        val tile = Tile(message.x, message.z, client.tile.height)
        if (!tile.viewableFrom(client.tile, Player.TILE_VIEW_DISTANCE)) {
            return
        }

        if (!client.lock.canGroundItemInteract() || !client.lock.canItemInteract()) {
            return
        }

        val item = client.inventory[message.slot] ?: return

        if (item.id != message.item) {
            return
        }

        val chunk = world.chunks.getOrCreate(tile)
        val groundItem = chunk.getEntities<GroundItem>(tile, EntityType.GROUND_ITEM).firstOrNull { it.item == message.groundItem && it.canBeViewedBy(client) } ?: return

        log(client, "Item on Ground Item action: item=[%d, %d], ground=[%d, %d], x=%d, z=%d, movement=%d",
                item.id, item.amount, groundItem.item, groundItem.amount, tile.x, tile.z, message.movementType)

        if (message.movementType == 1 && world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(groundItem.tile)
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_ITEM] = WeakReference(item)
        client.attr[INTERACTING_ITEM_ID] = item.id
        client.attr[INTERACTING_ITEM_SLOT] = message.slot
        client.attr[INTERACTING_OPT_ATTR] = GroundItemPathAction.ITEM_ON_GROUND_ITEM_OPTION
        client.attr[INTERACTING_GROUNDITEM_ATTR] = WeakReference(groundItem)
        client.executePlugin(GroundItemPathAction.walkPlugin)
    }
}