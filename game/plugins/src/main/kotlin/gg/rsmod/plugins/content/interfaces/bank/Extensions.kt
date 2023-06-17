package gg.rsmod.plugins.content.interfaces.bank

import gg.rsmod.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun Player.openBank() {
    Bank.open(this)
}