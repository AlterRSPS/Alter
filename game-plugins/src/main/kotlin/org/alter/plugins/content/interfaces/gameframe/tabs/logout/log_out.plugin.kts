package org.alter.plugins.content.interfaces.logout

import org.alter.game.model.timer.ACTIVE_COMBAT_TIMER

/**
 * Logout button.
 */
on_button(interfaceId = 182, component = 8) {
    if (!player.timers.has(ACTIVE_COMBAT_TIMER)) {
        player.requestLogout()
        player.write(net.rsprot.protocol.game.outgoing.logout.Logout)
        player.channelFlush()
    } else {
        player.message("You can't log out until 10 seconds after the end of combat.")
    }
}
