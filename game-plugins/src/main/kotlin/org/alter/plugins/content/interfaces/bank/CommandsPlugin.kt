package org.alter.plugins.content.interfaces.bank

import org.alter.game.model.priv.Privilege

onCommand("obank", Privilege.ADMIN_POWER) {
    player.openBank()
}

/**
 * Clears all bank tab varbits for the player, effectively
 * dumping all items back into the one main tab.
 */
onCommand("tabreset") {
    for (tab in 1..9)
        player.setVarbit(BankTabs.BANK_TAB_ROOT_VARBIT + tab, 0)
    player.setVarbit(BankTabs.SELECTED_TAB_VARBIT, 0)
}
