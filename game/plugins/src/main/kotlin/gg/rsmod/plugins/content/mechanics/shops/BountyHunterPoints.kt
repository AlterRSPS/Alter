package gg.rsmod.plugins.content.mechanics.shops

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.shop.PurchasePolicy
import gg.rsmod.game.model.shop.Shop
import gg.rsmod.game.model.shop.ShopCurrency
import gg.rsmod.game.model.shop.ShopItem
import gg.rsmod.plugins.api.ext.message

/**
 * @author Tom <rspsmods@gmail.com>
 */
open class BountyHunterPoints() : ShopCurrency {

    private data class AcceptItemState(val acceptable: Boolean, val errorMessage: String)

    private fun canAcceptItem(shop: Shop, world: World, points: Int): AcceptItemState {
        when {
            shop.purchasePolicy == PurchasePolicy.BUY_NONE -> return AcceptItemState(
                acceptable = false,
                errorMessage = "You can't sell any items to this shop."
            )
            else -> throw RuntimeException("Unhandled purchase policy. [shop=${shop.name}, policy=${shop.purchasePolicy}]")
        }
        return AcceptItemState(acceptable = true, errorMessage = "")
    }

    override fun onBuyValueMessage(p: Player, shop: Shop, item: Int) {
        val unnoted = Item(item).toUnnoted(p.world.definitions)
        val acceptance = canAcceptItem(shop, p.world, unnoted.id)
        if (acceptance.acceptable) {
            val shopItem = shop.items.filterNotNull().firstOrNull { it.item == item }
            val value = shopItem?.buyPrice ?: getBuyPrice(p.world, unnoted.id)
            val name = unnoted.getName(p.world.definitions)
            val currency = p.bountypoints
            p.message("$name: shop will buy for $value Bounty hunter Points")
        } else {
            p.message(acceptance.errorMessage)
        }
    }

    override fun onSellValueMessage(p: Player, shopItem: ShopItem) {
        val unnoted = Item(shopItem.item).toUnnoted(p.world.definitions)
        val value = shopItem.sellPrice ?: getSellPrice(p.world, unnoted.id)
        val name = unnoted.getName(p.world.definitions)
        val currency = p.bountypoints
        p.message("$name costs $value Bounty hunter Points")
    }

    override fun getSellPrice(world: World, item: Int): Int =
        Math.max(1, world.definitions.get(ItemDef::class.java, item).cost)

    override fun getBuyPrice(world: World, item: Int): Int =
        (world.definitions.get(ItemDef::class.java, item).cost * 0.6).toInt()

    override fun sellToPlayer(p: Player, shop: Shop, slot: Int, amt: Int) {
        val shopItem = shop.items[slot] ?: return

        val currencyCost = shopItem.sellPrice ?: getSellPrice(p.world, shopItem.item)
        val currencyCount = p.bountypoints

        var amount = Math.min(Math.floor(currencyCount.toDouble() / currencyCost.toDouble()).toInt(), amt)

        if (amount == 0) {
            p.message("You don't have enough Bounty hunter Points.")
            return
        }

        val moreThanStock = amount > shopItem.currentAmount

        amount = Math.min(amount, shopItem.currentAmount)

        if (amount == 0) {
            p.message("The shop has run out of stock.")
            return
        }

        if (moreThanStock) {
            p.message("The shop has run out of stock.")
        }

        val totalCost = currencyCost.toLong() * amount.toLong()
        if (totalCost > Int.MAX_VALUE) {
            return
        }

        if (p.bountypoints < totalCost) {
            p.message("You don't have enough Bounty hunter Points.")
            return
        }


        val add = p.inventory.add(item = shopItem.item, amount = amount, assureFullInsertion = false)
        if (add.completed == 0) {
            p.message("You don't have enough inventory space.")
        } else if(add.completed == 1) {
            val unnoted = Item(shopItem.item).toUnnoted(p.world.definitions)
            val name = unnoted.getName(p.world.definitions)
            p.removeBHP(totalCost.toInt())
            p.message("${name} Costed you ${totalCost.toInt()} Bounty hunter Points")
        }

        if (add.getLeftOver() > 0) {
            val refund = add.getLeftOver() * currencyCost
            p.addBHP(refund)
        }

        if (add.completed > 0 && shopItem.amount != Int.MAX_VALUE) {
            shop.items[slot]!!.currentAmount -= add.completed

            /*
             * Check if the item is temporary and should be removed from the shop.
             */
            if (shop.items[slot]?.amount == 0 && shop.items[slot]?.isTemporary == true) {
                shop.items[slot] = null
            }

            shop.refresh(p.world)
        }
    }

    override fun buyFromPlayer(p: Player, shop: Shop, slot: Int, amt: Int) {
        val item = p.inventory[slot] ?: return
        val unnoted = item.toUnnoted(p.world.definitions).id
        val acceptance = canAcceptItem(shop, p.world, unnoted)

        if (!acceptance.acceptable) {
            p.message(acceptance.errorMessage)
            return
        }

        val shopSlot = shop.items.indexOfFirst { it?.item == unnoted }
        val shopItem = if (shopSlot != -1) shop.items[shopSlot] else null
        val count = shopItem?.currentAmount ?: 0

        val amount = Math.min(Math.min(p.inventory.getItemCount(item.id), amt), Int.MAX_VALUE - count)

        if (count == 0 && shop.items.none { it == null } || amount == 0) {
            p.message("The shop has run out of space.")
            return
        }

        val remove = p.inventory.remove(item = item.id, amount = amount, assureFullRemoval = false)
        if (remove.completed == 0) {
            return
        }

        val price = shopItem?.buyPrice ?: getBuyPrice(p.world, unnoted)
        val compensation = Math.min(Int.MAX_VALUE.toLong(), price.toLong() * remove.completed.toLong()).toInt()
        val add = p.setBHP(p.bountypoints + compensation)
    }
}