package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("getvarbits", Privilege.DEV_POWER, description = "Get varbits for varp") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbits 83</col>") { values ->
        val varp = values[0].toInt()
        val varbits = mutableListOf<VarbitDef>()
        val totalVarbits = world.definitions.getCount(VarbitDef::class.java)
        for (i in 0 until totalVarbits) {
            val varbit = world.definitions.getNullable(VarbitDef::class.java, i)
            if (varbit?.varp == varp) {
                varbits.add(varbit)
            }
        }
        player.message("Varbits for varp <col=801700>$varp</col>:")
        varbits.forEach { varbit ->
            player.message("  ${varbit.id} [bits ${varbit.startBit}-${varbit.endBit}] [current ${player.getVarbit(varbit.id)}]")
        }
    }
}