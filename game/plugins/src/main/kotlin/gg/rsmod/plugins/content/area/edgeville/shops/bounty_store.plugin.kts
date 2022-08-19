package gg.rsmod.plugins.content.area.edgeville.shops


import gg.rsmod.plugins.content.mechanics.shops.BountyHunterPoints

create_shop("Emblem Trader", BountyHunterPoints(), purchasePolicy = PurchasePolicy.BUY_NONE) {
    items[0] = ShopItem(Items.HUNTERS_HONOUR, 10, sellPrice = 2500)
    items[1] = ShopItem(Items.ROGUES_REVENGE, 10, sellPrice = 2500)
    items[2] = ShopItem(Items.LOOTING_BAG, 10, sellPrice = 10)
    items[3] = ShopItem(Items.RUNE_POUCH, 10, sellPrice = 1200)
}