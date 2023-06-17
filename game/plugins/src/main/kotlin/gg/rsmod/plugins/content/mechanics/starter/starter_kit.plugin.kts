package gg.rsmod.plugins.content.mechanics.starter

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR

on_login {
    if (player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login) {
        with (player.inventory) {
            add(Items.LOGS, 5)
            add(Items.TINDERBOX)
            add(Items.BREAD, 5)
            add(Items.BRONZE_PICKAXE)
            add(Items.BRONZE_DAGGER)
            add(Items.KNIFE)
        }
    }
}
