import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("spellbook", Privilege.DEV_POWER, description = "Switch between spellbooks") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::spellbook 1</col>") { values ->
        val id = values[0].toInt()
        if (id > 3) {
            player.message("SpellBook does not exist.")
        }
        player.setVarbit(4070, id)
    }
}
