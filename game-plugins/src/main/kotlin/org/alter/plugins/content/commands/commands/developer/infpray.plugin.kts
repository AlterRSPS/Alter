package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.bits.*
import org.alter.game.model.priv.Privilege

on_command("infpray", Privilege.DEV_POWER, description = "Infinite prayer points") {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.PRAY)
    player.message("Infinite prayer: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.PRAY)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}