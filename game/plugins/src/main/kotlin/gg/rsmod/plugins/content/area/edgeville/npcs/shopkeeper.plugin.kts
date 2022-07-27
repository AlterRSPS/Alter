package gg.rsmod.plugins.content.area.edgeville.shopkeeper.npcs

arrayOf(Npcs.SHOP_KEEPER, Npcs.SHOP_KEEPER_2821, Npcs.SHOP_ASSISTANT_2822, Npcs.SHOP_KEEPER_7769, Npcs.SHOP_KEEPER_2823, Npcs.SHOP_ASSISTANT_2824, Npcs.SHOP_KEEPER_2817,
Npcs.SHOP_ASSISTANT_2818, Npcs.SHOP_KEEPER_7913, Npcs.BANDIT_SHOPKEEPER, Npcs.GHOST_SHOPKEEPER, Npcs.SHOP_KEEPER_2884, Npcs.SHOP_ASSISTANT_2885,
Npcs.SHOP_KEEPER_2815, Npcs.SHOP_ASSISTANT_2816, Npcs.SHOP_KEEPER_2825, Npcs.SHOP_ASSISTANT_2826, Npcs.FAIRY_SHOP_KEEPER, Npcs.FAIRY_SHOP_ASSISTANT,
Npcs.SHOP_KEEPER_2888, Npcs.SHOP_KEEPER_2894).forEach { shopkeeper ->

        on_npc_option(npc = shopkeeper, option = "Talk-to", lineOfSightDistance = 3) {
            player.queue {
                dialog(this)
            }
        }
        on_npc_option(npc = shopkeeper, option = "Trade", lineOfSightDistance = 0) {
            player.queue {
                player.openShop("General Store")
            }
    }

}

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Good day")
    it.chatNpc("how may I help you?")
    when (options(it)) {
        1 -> it.player.openShop("General Store")

    }
}

suspend fun options(it: QueueTask): Int = it.options("What do you have for sale?", "No thanks.")

