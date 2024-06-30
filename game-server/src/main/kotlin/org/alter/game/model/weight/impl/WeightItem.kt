package org.alter.game.model.weight.impl

import org.alter.game.model.item.Item
import org.alter.game.model.weight.WeightNode
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
open class WeightItem(
    val item: Int,
    private val amount: Int = 1,
    private val maxAmount: Int = amount,
    weight: Int,
) : WeightNode<Item>(weight) {
    constructor(item: Int, amount: IntRange, weight: Int) : this(item, amount.start, amount.last, weight)

    override fun convert(random: Random): Item = Item(item, amount + random.nextInt((maxAmount - amount) + 1))
}
