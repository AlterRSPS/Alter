import gg.rsmod.game.model.priv.Privilege

/**
 * @author Fritz <frikkipafi@gmail.com>
 */

on_command("img", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    val key = args[0].toInt()
    player.world.players.forEach {
        player.message("<img=${key}>", ChatMessageType.ENGINE)
    }
}