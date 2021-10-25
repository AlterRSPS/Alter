import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("home", Privilege.ADMIN_POWER) {
    val home = world.gameContext.home
    player.moveTo(home)
}