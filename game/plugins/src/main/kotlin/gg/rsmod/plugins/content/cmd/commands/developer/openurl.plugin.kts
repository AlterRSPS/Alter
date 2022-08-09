import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("openurl", Privilege.DEV_POWER, description = "Open url") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openurl google.com</col>") { values ->
        val url = values[0]
        if(!url.startsWith("http://") || !url.startsWith("https://"))
            player.openUrl("https://$url") // not perfect by any means, but simple enough as fallback for easier command
        else
            player.openUrl(url)
    }
}