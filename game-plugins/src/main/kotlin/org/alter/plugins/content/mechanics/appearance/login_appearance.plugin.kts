package org.alter.plugins.content.mechanics.appearance

import org.alter.game.model.attr.APPEARANCE_SET_ATTR

on_login {
    if (player.attr[APPEARANCE_SET_ATTR] == false) {
        player.queue(TaskPriority.STRONG) {
            player.lock = LockState.FULL_WITH_LOGOUT
            selectAppearance()
        }
    }
}