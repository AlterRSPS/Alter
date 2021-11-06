import gg.rsmod.plugins.content.inter.emotes.EmotesTab

on_login() {
    player.message("All emote varbit's were set to 1.");
    EmotesTab.unlockAll(player)
}
