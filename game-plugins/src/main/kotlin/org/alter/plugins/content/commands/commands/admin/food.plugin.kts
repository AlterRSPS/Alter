import org.alter.game.model.priv.Privilege

onCommand("food", Privilege.ADMIN_POWER, description = "Fills your inventory with food") {
    player.inventory.add(item = "item.manta_ray", amount = player.inventory.freeSlotCount)
}
