package gg.rsmod.plugins.content.area.edgeville

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency
import gg.rsmod.game.model.shop.PurchasePolicy


create_shop("General Store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
            items[1] = ShopItem(Items.TINDERBOX, 2)
            items[2] = ShopItem(Items.CHISEL, 2)
            items[3] = ShopItem(Items.HAMMER, 5)
        }