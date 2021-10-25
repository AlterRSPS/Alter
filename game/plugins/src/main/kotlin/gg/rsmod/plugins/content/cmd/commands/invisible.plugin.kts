import gg.rsmod.game.model.priv.Privilege

on_command("invisible", Privilege.ADMIN_POWER) {
    player.invisible = !player.invisible
    player.message("Invisible: ${if (!player.invisible) "<col=801700>false</col>" else "<col=178000>true</col>"}")
}