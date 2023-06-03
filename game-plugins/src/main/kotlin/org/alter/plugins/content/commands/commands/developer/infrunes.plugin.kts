package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.magic.MagicSpells

on_command("infrunes", Privilege.DEV_POWER, description = "Infinite runes") {
    player.toggleVarbit(MagicSpells.INF_RUNES_VARBIT)
    player.message("Infinite runes: ${if (player.getVarbit(MagicSpells.INF_RUNES_VARBIT) != 1) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}