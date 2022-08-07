import gg.rsmod.game.model.priv.Privilege

on_command(command = "cmds", powerRequired = Privilege.ADMIN_POWER, description = "Display commands") {
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.openInterface(187, dest = InterfaceDestination.MAIN_SCREEN)
    player.runClientScript(217, "Command List:", get_all_commands().joinToString("|").removePrefix("(").removePrefix(")"))
}