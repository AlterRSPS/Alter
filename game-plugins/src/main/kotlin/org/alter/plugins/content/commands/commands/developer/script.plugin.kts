import org.alter.api.ClientScript
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.tryWithUsage

onCommand("script", Privilege.DEV_POWER, description = "Run script by id") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::script id args...</col>") { values ->
        val id = values[0].toInt()
        val clientArgs = MutableList<Any>(values.size - 1) {}
        for (arg in 1 until values.size)
            clientArgs[arg - 1] = values[arg].toIntOrNull() ?: values[arg]
        player.runClientScript(ClientScript(id = id), *clientArgs.toTypedArray())
        player.message("Executing <col=0000FF>cs_$id</col><col=801700>$clientArgs</col>")
    }
}
