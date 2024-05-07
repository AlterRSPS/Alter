package org.alter.game.message.impl

import org.alter.game.message.Message
import org.alter.game.model.item.Item

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateInvFullMessage : Message {

    val items: Array<Item?>
    val componentHash: Int
    val containerKey: Int

    private constructor(items: Array<Item?>, componentHash: Int, containerKey: Int) {
        this.items = items
        this.componentHash = componentHash
        this.containerKey = containerKey
    }

    constructor(interfaceId: Int, componentId: Int, inventoryId: Int, items: Array<Item?>) : this(items, (interfaceId shl 16) or componentId, inventoryId)

    constructor(interfaceId: Int, componentId: Int, items: Array<Item?>) : this(items, (interfaceId shl 16) or componentId, 0)

    constructor(containerKey: Int, items: Array<Item?>) : this(items, -1, containerKey)
}