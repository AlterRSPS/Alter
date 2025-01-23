import org.alter.plugins.content.commands.Commands_plugin.tryWithUsage

/**
 *   @Author Cl0ud
 */
onCommand("setrights") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Cba") { values ->
        val privilege = values[0].toInt()
        player.privilege = world.privileges.get(privilege)!!
    }
}
