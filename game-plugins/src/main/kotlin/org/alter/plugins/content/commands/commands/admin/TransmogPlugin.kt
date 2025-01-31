import org.alter.game.model.priv.Privilege

onCommand("transmog", Privilege.ADMIN_POWER, description = "Transmog yourself") {
    val args = player.getCommandArgs()
    val id = args[0].toInt()
    player.setTransmogId(id)
    player.message("It's morphing time!")
}
