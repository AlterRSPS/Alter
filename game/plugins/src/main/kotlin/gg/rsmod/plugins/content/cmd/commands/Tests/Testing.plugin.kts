import gg.rsmod.game.message.impl.CamResetMessage
import gg.rsmod.game.message.impl.CamShakeMessage
import gg.rsmod.plugins.content.cmd.Commands_plugin


on_command("camshake") {
    val args = player.getCommandArgs()
    Commands_plugin.Command.tryWithUsage(player, args, "Invalid format!") { values ->
        try {
            val index = values[0].toInt()
            val left = values[1].toInt()
            val center = values[2].toInt()
            val right = values[3].toInt()
            player.write(CamShakeMessage(index, left, center, right))
            player.message("CamShakePacketSent[$index, $left, $center, $right]")
        } catch (e: Exception) {
            player.message(e.toString())
        }
    }
}

on_command("camshakereset") {
    player.write(CamResetMessage())
    player.message("Camera Shake Restarted")
}


