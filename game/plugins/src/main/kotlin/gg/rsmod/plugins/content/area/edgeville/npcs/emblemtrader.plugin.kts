package gg.rsmod.plugins.content.area.edgeville.npcs

arrayOf(Npcs.EMBLEM_TRADER, Npcs.EMBLEM_TRADER_7943).forEach { emblemtrader ->

    on_npc_option(npc = emblemtrader, option = "Talk-to", lineOfSightDistance = 0) {
        player.queue {
            NpcPlaceHolder().dialog(this)
        }
    }
    on_npc_option(npc = emblemtrader, option = "Rewards", lineOfSightDistance = 0) {
        player.queue {
            player.openShop("Emblem Trader")
            player.message("You have ${player.bountypoints} Bounty hunter Points to spend")
        }
    }
    on_npc_option(npc = emblemtrader, option = "Coffer", lineOfSightDistance = 0) {
        player.queue {
            NpcPlaceHolder().dialog(this)
        }
    }
    on_npc_option(npc = emblemtrader, option = "Skull", lineOfSightDistance = 0) {
        player.queue {
            if (!player.hasSkullIcon(SkullIcon.NONE)) {
                player.setSkullIcon(SkullIcon.NONE)
            } else player.setSkullIcon(SkullIcon.WHITE)

        }
    }
}
