package org.alter.plugins.content.interfaces.bank

import org.alter.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun Player.openBank() {
    Bank.open(this)
}
