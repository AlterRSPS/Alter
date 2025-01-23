import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.tryWithUsage

onCommand("npc", Privilege.ADMIN_POWER, description = "Spawn Npc") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::npc 1</col>") { values ->
        val id = values[0].toInt()
        val npc = Npc(id, player.tile, world)
        player.message("NPC: $id , on x:${player.tile.x} y:${player.tile.z}")
        world.spawn(npc)
    }
}
