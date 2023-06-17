import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.interfaces.emotes.EmotesTab

on_command("emotes", Privilege.DEV_POWER, description = "Unlock all emotes") {
    EmotesTab.unlockAll(player)
    player.message("All emotes were unlocked.")
}
