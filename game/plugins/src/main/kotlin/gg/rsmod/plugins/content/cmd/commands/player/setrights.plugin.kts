import gg.rsmod.plugins.content.cmd.Commands_plugin

/**
 *   @Author Cl0ud
 */
on_command("setrights") {
    val args = player.getCommandArgs()
    Commands_plugin.Command.tryWithUsage(player, args, "Cba") { values ->
        val privilege = values[0].toInt()
        player.privilege = world.privileges.get(privilege)!!
    }
}
