package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.bits.*
import gg.rsmod.game.model.priv.Privilege

on_command("infhp", Privilege.DEV_POWER, description = "Infinite HP") {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.HP)
    player.message("Infinite hp: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.HP)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}