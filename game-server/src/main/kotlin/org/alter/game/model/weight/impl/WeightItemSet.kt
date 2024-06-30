package org.alter.game.model.weight.impl

import org.alter.game.model.item.Item
import org.alter.game.model.weight.WeightNode
import org.alter.game.model.weight.WeightNodeSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WeightItemSet : WeightNodeSet<Item>() {
    override fun add(node: WeightNode<Item>): WeightItemSet {
        super.add(node)
        return this
    }
}
