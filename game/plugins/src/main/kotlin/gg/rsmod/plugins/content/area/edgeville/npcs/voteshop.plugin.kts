package gg.rsmod.plugins.content.area.edgeville.shopkeeper.npcs

arrayOf(Npcs.PIERRE).forEach { shopkeeper ->

        on_npc_option(npc = shopkeeper, option = "Talk-to", lineOfSightDistance = 0) {
            player.queue {
                player.openShop("Vote Store")
                    val ticketCount = player.inventory.getItemCount(Items.VOTE_TICKET)
                    if (ticketCount == 0) {
                        player.message("You don't have any tickets to spend. Vote for us and get some with <col=9400D3>::vote</col>!")
                    } else {
                        player.message("You currently have <col=800000>$ticketCount</col> tickets to spend in this shop.")
                }
            }
    }

}

