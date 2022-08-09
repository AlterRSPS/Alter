package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.attr.NO_CLIP_ATTR
import gg.rsmod.game.model.priv.Privilege

on_command("noclip", Privilege.DEV_POWER, description = "Noclip, Superpowers?") {
    val noClip = !(player.attr[NO_CLIP_ATTR] ?: false)
    player.attr[NO_CLIP_ATTR] = noClip
    player.message("No-clip: ${if (noClip) "<col=178000>enabled</col>" else "<col=801700>disabled</col>"}")
}