import gg.rsmod.game.model.priv.Privilege

/**
 * @author Fritz <frikkipafi@gmail.com>
 */

on_command("img", Privilege.ADMIN_POWER, description = "Show chat images by id") {
    val args = player.getCommandArgs()
    val key = args[0].toInt()
    player.message("<img=${key}>", ChatMessageType.ENGINE)
}