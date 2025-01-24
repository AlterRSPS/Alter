import org.alter.game.model.priv.Privilege

onCommand("spellbook", Privilege.DEV_POWER, description = "Switch between spellbooks") {
    val args = player.getCommandArgs()
        val id = args[0].toInt()
        if (id > 3) {
            player.message("SpellBook does not exist.")
        }
        player.setVarbit(4070, id)
}
