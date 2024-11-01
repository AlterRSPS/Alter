package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.locs.OpLocT
import org.alter.game.model.move.ObjectPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.attr.INTERACTING_ITEM
import org.alter.game.model.attr.INTERACTING_ITEM_SLOT
import org.alter.game.model.attr.INTERACTING_OBJ_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Player
import org.alter.game.model.move.moveTo
import org.alter.game.model.move.stopMovement
import org.alter.game.model.priv.Privilege
import java.lang.ref.WeakReference

class OpLocTHandler : MessageHandler<OpLocT> {
    override fun consume(
        client: Client,
        message: OpLocT,
    ) {
        val slot = message.selectedSub
        val sobj = message.selectedObj
        val sloc = message.id

        if (slot < 0 || slot >= client.inventory.capacity) {
            return
        }

        if (!client.lock.canItemInteract()) {
            return
        }

        val item = client.inventory[slot] ?: return

        if (item.id != sobj) {
            return
        }

        // If the player can't move, do nothing
        if (!client.lock.canMove()) {
            return
        }

        // If the tile is too far away, do nothing
        val tile = Tile(message.x, message.z, client.tile.height)
        if (!tile.viewableFrom(client.tile, Player.TILE_VIEW_DISTANCE)) {
            return
        }

        // Get the region chunk that the object would belong to.
        val chunk = client.world.chunks.getOrCreate(tile)
        val obj =
            chunk.getEntities<GameObject>(tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull {
                it.id == message.id
            } ?: return

        if (message.controlKey && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            val def = obj.getDef()
            client.moveTo(
                client.world.findRandomTileAround(obj.tile, radius = 1, centreWidth = def.sizeX, centreLength = def.sizeY) ?: obj.tile,
            )
        }

        log(client, "Item on object: item=%d, slot=%d, obj=%d, x=%d, y=%d", sobj, slot, sloc, message.x, message.z)

        client.stopMovement()
        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_ITEM] = WeakReference(item)
        client.attr[INTERACTING_ITEM_SLOT] = slot
        client.attr[INTERACTING_OBJ_ATTR] = WeakReference(obj)

        client.executePlugin(ObjectPathAction.itemOnObjectPlugin)
    }
}
