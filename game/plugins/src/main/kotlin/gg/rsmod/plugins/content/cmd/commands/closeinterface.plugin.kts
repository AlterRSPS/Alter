import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("closeinterface", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::closeinterface interfaceId</col>") { values ->
        val interfaceId = values[0].toInt()
        player.closeInterface(interfaceId)
        player.message("Closing interface <col=801700>$interfaceId</col>")
    }
}