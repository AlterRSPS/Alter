import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin

val warps = mutableListOf<Tile>()


on_world_init {
 // Read warp list
}

on_command("addwarp", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    Commands_plugin.Command.tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::addwarp edgeville</col> or <col=801700>::addwarp edgeville 3086 3500 0</col>") { values ->

    }
}

on_command("removewarp") {}

on_command("warplist", Privilege.ADMIN_POWER) {}

on_command("warp", Privilege.ADMIN_POWER) {}