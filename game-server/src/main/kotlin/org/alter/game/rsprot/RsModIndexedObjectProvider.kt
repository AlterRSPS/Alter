package org.alter.game.rsprot

import net.rsprot.protocol.common.game.outgoing.inv.InventoryObject
import net.rsprot.protocol.game.outgoing.inv.UpdateInvPartial
import org.alter.game.model.item.Item

class RsModIndexedObjectProvider(indices: Iterator<Int>, val items: Array<Item?>) : UpdateInvPartial.IndexedObjectProvider(indices) {
    override fun provide(slot: Int): Long {
        val item = items[slot] ?: return InventoryObject(slot, -1, -1)
        return InventoryObject(slot, item.id, item.amount)
    }
}
