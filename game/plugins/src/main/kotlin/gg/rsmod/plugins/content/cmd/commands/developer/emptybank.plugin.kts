import gg.rsmod.game.model.priv.Privilege

on_command("emptybank", Privilege.DEV_POWER) {
    player.bank.removeAll()
}