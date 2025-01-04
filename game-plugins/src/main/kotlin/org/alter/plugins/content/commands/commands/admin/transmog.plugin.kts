import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

onCommand("transmog", Privilege.ADMIN_POWER, description = "Transmog yourself") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::transmog transmogId</col>") { values ->
        val id = values[0].toInt()
        player.setTransmogId(id)
        player.message("It's morphing time!")
    }
}
