import org.alter.game.model.priv.Privilege

onCommand("openurl", Privilege.DEV_POWER, description = "Open url") {
    val args = player.getCommandArgs()
    val url = args[0]
    if (!url.startsWith("http://") || !url.startsWith("https://")) {
        player.openUrl("https://$url") // not perfect by any means, but simple enough as fallback for easier command
    } else {
        player.openUrl(url)
    }
}
