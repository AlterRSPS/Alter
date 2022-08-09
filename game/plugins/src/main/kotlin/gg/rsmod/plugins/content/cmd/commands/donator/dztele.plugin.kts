package gg.rsmod.plugins.content.cmd.commands.donator

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport
import gg.rsmod.plugins.content.magic.prepareForTeleport

on_command("dz", Privilege.DONOR_POWER, description = "Donatorzone teleport") {
    player.queue(TaskPriority.STRONG) {
        player.teleport(this)
    }
}
on_command("donator", Privilege.ADMIN_POWER) {
    player.queue(TaskPriority.STRONG) {
        player.teleport(this)
    }
}

suspend fun Player.teleport(it : QueueTask) {
    if (canTeleport(TeleportType.MODERN)) {
        prepareForTeleport()
        lock = LockState.FULL_WITH_DAMAGE_IMMUNITY
        animate(id = 4069, delay = 16)
        playSound(id = 965, volume = 1, delay = 15)
        it.wait(cycles = 3)
        graphic(id = 678)
        animate(id = 4071)
        it.wait(cycles = 2)
        animate(id = -1)
        unlock()
        it.player.moveTo(Tile(x = 2150, z = 5098, height = 1))
    }
}
