package org.alter.plugins.content.mechanics.skullremoval

import org.alter.game.model.timer.SKULL_ICON_DURATION_TIMER

onTimer(SKULL_ICON_DURATION_TIMER) {
    if (!player.hasSkullIcon(SkullIcon.NONE)) {
        player.setSkullIcon(SkullIcon.NONE)
    }
}
