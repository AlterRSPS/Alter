package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.objs.OpObjT
import org.alter.game.model.move.ObjectPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.model.Tile
import org.alter.game.model.entity.Client

class OpObjTHandler : MessageHandler<OpObjT> {
    override fun consume(
        client: Client,
        message: OpObjT,
    ) {
        /**
         * What to keep in mind:
         * This is used by:
         *  Item on ground item
         *  Spell on ground item
         *  Spell on Npc?
         *
         *  Could actually create global Interaction Range Attribute
         */
        val slot = message.selectedSub
        val sobj = message.selectedObj

        // If the tile is too far away, do nothing
        val tile = Tile(message.x, message.z, client.tile.height)
        log(
            client,
            "Item on object: item=%d, slot=%d, obj=%d, x=%d, y=%d",
            sobj,
            slot,
            message.id,
            message.x,
            message.z
        )
        client.executePlugin(ObjectPathAction.itemOnObjectPlugin)

    }
}
