package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.magic.MagicSpells

on_command("infrunes", Privilege.DEV_POWER, description = "Infinite runes") {
    player.toggleVarbit(MagicSpells.INF_RUNES_VARBIT)
    player.message("Infinite runes: ${if (player.getVarbit(MagicSpells.INF_RUNES_VARBIT) != 1) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}