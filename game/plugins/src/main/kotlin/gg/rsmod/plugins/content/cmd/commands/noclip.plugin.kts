import gg.rsmod.game.model.attr.NO_CLIP_ATTR
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("noclip", Privilege.ADMIN_POWER) {
    val noClip = !(player.attr[NO_CLIP_ATTR] ?: false)
    player.attr[NO_CLIP_ATTR] = noClip
    player.message("No-clip: ${if (noClip) "<col=178000>enabled</col>" else "<col=801700>disabled</col>"}")
}