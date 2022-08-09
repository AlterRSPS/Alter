package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("invisible", Privilege.DEV_POWER, description = "Become a ghost?") {
    player.invisible = !player.invisible
    player.message("Invisible: ${if (!player.invisible) "<col=801700>false</col>" else "<col=178000>true</col>"}")
}