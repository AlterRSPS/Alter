import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("npc", Privilege.ADMIN_POWER, description = "Spawn Npc") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::npc 1</col>") { values ->
        val id = values[0].toInt()
        val npc = Npc(id, player.tile, world)
        player.message("NPC: $id , on x:${player.tile.x} z:${player.tile.z}");
        world.spawn(npc)
    }
}