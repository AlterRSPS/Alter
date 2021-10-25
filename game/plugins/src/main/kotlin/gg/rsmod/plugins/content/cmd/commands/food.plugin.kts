import gg.rsmod.game.model.priv.Privilege

on_command("food", Privilege.ADMIN_POWER) {
    player.inventory.add(item = Items.MANTA_RAY, amount = player.inventory.freeSlotCount)
}