import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("teler", Privilege.DEV_POWER, description = "Teleport to region") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::teler 12850</col>") { values ->
        val region = values[0].toInt()
        val tile = Tile.fromRegion(region)
        player.moveTo(tile)
    }
}