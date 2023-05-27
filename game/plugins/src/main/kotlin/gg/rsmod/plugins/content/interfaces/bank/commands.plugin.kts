package gg.rsmod.plugins.content.interfaces.bank

import gg.rsmod.game.model.priv.Privilege

on_command("obank", Privilege.ADMIN_POWER) {
    player.openBank()
}

/**
 * Clears all bank tab varbits for the player, effectively
 * dumping all items back into the one main tab.
 */
on_command("tabreset"){
    for(tab in 1..9)
        player.setVarbit(BankTabs.BANK_TAB_ROOT_VARBIT +tab, 0)
    player.setVarbit(BankTabs.SELECTED_TAB_VARBIT, 0)
}
/**
 * @TODO
 * When withdraw is done -> that tabs value does not get changed.
 */
on_command("tabinfo") {
    for (tab in 0..9) {
        val t = player.getVarbit(BankTabs.SELECTED_TAB_VARBIT+tab)
        if (t != 0) {
            player.message("[${BankTabs.SELECTED_TAB_VARBIT+tab}][$tab] = $t")
        }
        BankTabs.getItemsFromTab(player, tab)?.sortedBy { it.slot }?.forEach {
            println("$it")
        }
    }
}

