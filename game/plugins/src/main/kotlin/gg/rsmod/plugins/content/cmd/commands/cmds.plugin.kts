import gg.rsmod.game.model.priv.Privilege

on_command(command = "cmds", powerRequired = Privilege.ADMIN_POWER) {
    player.openInterface(187, dest = InterfaceDestination.MAIN_SCREEN)
    val commands = get_all_commands().joinToString("|").removePrefix("(").removePrefix(")")
    player.runClientScript(217, "Command List:", commands)
}