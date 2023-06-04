import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

/**
 *   @Author Cl0ud
 */
on_command("setrights") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Cba") { values ->
        val privilege = values[0].toInt()
        player.privilege = world.privileges.get(privilege)!!
    }
}
