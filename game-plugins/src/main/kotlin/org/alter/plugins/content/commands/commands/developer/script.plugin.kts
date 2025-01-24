import org.alter.api.ClientScript
import org.alter.game.model.priv.Privilege

onCommand("script", Privilege.DEV_POWER, description = "Run script by id") {
    val values = player.getCommandArgs()
    val id = values[0].toInt()
    val clientArgs = MutableList<Any>(values.size - 1) {}
    for (arg in 1 until values.size)
        clientArgs[arg - 1] = values[arg].toIntOrNull() ?: values[arg]
    player.runClientScript(ClientScript(id = id), *clientArgs.toTypedArray())
    player.message("Executing <col=0000FF>cs_$id</col><col=801700>$clientArgs</col>")
}
