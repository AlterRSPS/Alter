package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.objs.OpObj
import org.alter.game.model.move.GroundItemRouteAction
import org.alter.game.message.MessageHandler
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.attr.CLIENT_KEY_COMBINATION
import org.alter.game.model.attr.INTERACTING_GROUNDITEM_ATTR
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Player
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpObjHandler : MessageHandler<OpObj> {
    override fun consume(
        client: Client,
        message: OpObj,
    ) {
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
        log(
            client,
            "Ground Item action %d: item=%d, x=%d, y=%d, movement=%b",
            message.op,
            message.id,
            message.x,
            message.z,
            message.controlKey,
        )
        /*
         * Get the region chunk that the object would belong to.
         */
        val chunk = client.world.chunks.getOrCreate(tile)
        val item =
            chunk.getEntities<GroundItem>(tile, EntityType.GROUND_ITEM).firstOrNull {
                it.item == message.id && it.canBeViewedBy(client)
            } ?: return
        client.attr[CLIENT_KEY_COMBINATION] = if (message.controlKey) 2 else 0
        client.attr[INTERACTING_OPT_ATTR] = message.op
        client.attr[INTERACTING_GROUNDITEM_ATTR] = WeakReference(item)
        client.executePlugin(GroundItemRouteAction.walkPlugin)
    }
}
