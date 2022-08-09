package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.bits.*
import gg.rsmod.game.model.priv.Privilege

on_command("infrun", Privilege.DEV_POWER, description = "Infinite run energy") {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)
    player.message("Infinite run: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}