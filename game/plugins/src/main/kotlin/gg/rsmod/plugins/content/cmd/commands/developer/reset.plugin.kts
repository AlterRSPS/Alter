package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("reset", Privilege.DEV_POWER, description = "Reset all skills to lowest level") {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, if (i == Skills.HITPOINTS) 10 else 1)
    }
    player.calculateAndSetCombatLevel()
}