package gg.rsmod.plugins.content.area.edgeville.handlers


create_shop("Vote Store", VoteCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
    items[1] = ShopItem(Items.TINDERBOX, 2)
    items[2] = ShopItem(Items.CHISEL, 2)
    items[3] = ShopItem(Items.HAMMER, 5)
}