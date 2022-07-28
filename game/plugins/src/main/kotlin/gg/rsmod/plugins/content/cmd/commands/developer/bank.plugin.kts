import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.inter.bank.openBank

on_command("openbank", Privilege.DEV_POWER) {
    player.openBank()
}