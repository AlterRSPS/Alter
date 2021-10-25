import gg.rsmod.game.model.attr.FREE_BOND_CLAIMED_ATTR
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage
import java.util.*

on_command("lastbond") {
    player.message("last bond claimed: ${Date(player.attr[FREE_BOND_CLAIMED_ATTR]!!.toLong())}")
}