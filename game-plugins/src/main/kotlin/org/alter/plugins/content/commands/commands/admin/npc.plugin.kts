import org.alter.game.model.priv.Privilege

onCommand("npc", Privilege.ADMIN_POWER, description = "Spawn Npc") {
    val values = player.getCommandArgs()
    val id = values[0].toInt()
    val npc = Npc(id, player.tile, world)
    player.message("NPC: $id , on x:${player.tile.x} y:${player.tile.z}")
    world.spawn(npc)
}
