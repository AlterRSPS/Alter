import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("cmds", Privilege.ADMIN_POWER) {
    val command = get_all_commands().joinToString()
    val messages = ArrayList<String>()
    var buf = ""
    val split = command.split(", ")
    split.forEach { s ->
        buf += s
        buf += ", "
        if (buf.length > 75) {
            messages.add(buf)
            buf = ""
        }
    }
    if(buf != "") {
        buf = buf.substring(0, buf.length-2)
        messages.add(buf)
    }
    player.message("Commands:")

    messages.forEach {
            s -> player.message(s)
    }
}