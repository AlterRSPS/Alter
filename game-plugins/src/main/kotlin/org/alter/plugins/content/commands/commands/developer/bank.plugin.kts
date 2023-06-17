import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.interfaces.bank.openBank

on_command("openbank", Privilege.DEV_POWER, description = "Open bank") {
    player.openBank()
}