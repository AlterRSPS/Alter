package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.attr.NO_CLIP_ATTR
import org.alter.game.model.priv.Privilege

onCommand("noclip", Privilege.DEV_POWER, description = "Noclip, Superpowers?") {
    val noClip = !(player.attr[NO_CLIP_ATTR] ?: false)
    player.attr[NO_CLIP_ATTR] = noClip
    player.message("No-clip: ${if (noClip) "<col=178000>enabled</col>" else "<col=801700>disabled</col>"}")
}
