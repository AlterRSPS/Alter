import gg.rsmod.game.model.priv.Privilege

on_command("master", Privilege.ADMIN_POWER) {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, 99)
    }
    player.calculateAndSetCombatLevel()
}