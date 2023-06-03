import org.alter.plugins.content.commands.Commands_plugin

/**
 *   @Author Cl0ud
 */
on_command("setrights") {
    val args = player.getCommandArgs()
    gg.rsmod.plugins.content.commands.Commands_plugin.Command.tryWithUsage(player, args, "Cba") { values ->
        val privilege = values[0].toInt()
        player.privilege = world.privileges.get(privilege)!!
    }
}
