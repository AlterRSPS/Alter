package org.alter.plugins.content.commands.commands.developer

import dev.openrune.cache.CacheManager.getVarbits
import dev.openrune.cache.CacheManager.varbitSize
import dev.openrune.cache.filestore.definition.data.VarBitType
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("getvarbits", Privilege.DEV_POWER, description = "Get varbits for varp") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbits 83</col>") { values ->
        val varp = values[0].toInt()
        val varbits = mutableListOf<VarBitType>()
        val totalVarbits = varbitSize()
        for (i in 0 until totalVarbits) {
            val varbit = getVarbits().get(i)
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