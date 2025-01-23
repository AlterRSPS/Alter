import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.tryWithUsage

onCommand("teler", Privilege.DEV_POWER, description = "Teleport to region") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::teler 12850</col>") { values ->
        val region = values[0].toInt()
        val tile = Tile.fromRegion(region)
        player.moveTo(tile)
    }
}
