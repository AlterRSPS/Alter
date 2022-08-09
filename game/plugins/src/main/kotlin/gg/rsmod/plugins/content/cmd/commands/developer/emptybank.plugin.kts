import gg.rsmod.game.model.priv.Privilege

on_command("emptybank", Privilege.DEV_POWER, description = "Empty your bank") {
    player.bank.removeAll()
}