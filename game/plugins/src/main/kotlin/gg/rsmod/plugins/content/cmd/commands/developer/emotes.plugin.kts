import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.inter.emotes.EmotesTab

on_command("emotes", Privilege.DEV_POWER, description = "Unlock all emotes") {
    EmotesTab.unlockAll(player)
}
