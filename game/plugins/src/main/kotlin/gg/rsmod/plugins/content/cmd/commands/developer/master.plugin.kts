package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("master", Privilege.DEV_POWER) {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, 99)
    }
    player.calculateAndSetCombatLevel()
}