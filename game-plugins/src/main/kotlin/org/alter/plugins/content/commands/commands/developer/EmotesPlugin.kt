import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.interfaces.emotes.EmotesTab

onCommand("emotes", Privilege.DEV_POWER, description = "Unlock all emotes") {
    EmotesTab.unlockAll(player)
    player.message("All emotes were unlocked.")
}
