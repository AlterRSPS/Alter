package gg.rsmod.plugins.content.area.shops


import gg.rsmod.plugins.content.mechanics.shops.BountyHunterPoints

create_shop("Emblem Trader", BountyHunterPoints(), purchasePolicy = PurchasePolicy.BUY_NONE) {
    items[1] = ShopItem(Items.TINDERBOX, 2, sellPrice = 10)
    items[2] = ShopItem(Items.CHISEL, 2, sellPrice = 10)
    items[3] = ShopItem(Items.HAMMER, 5, sellPrice = 10)
}