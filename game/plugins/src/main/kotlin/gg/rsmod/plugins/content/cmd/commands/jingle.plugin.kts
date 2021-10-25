import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("jingle", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::jingle 1</col>") { values ->
        val id = values[0].toInt()
        player.playJingle(id)
        player.message("Jingle: $id")
    }
}