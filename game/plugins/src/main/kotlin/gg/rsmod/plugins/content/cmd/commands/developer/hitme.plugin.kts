package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("hitme", Privilege.DEV_POWER, description = "Hitsplash by id and amount") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::hitme hitType amount</col>") { values ->
        val hitType = HitType.get(values[0].toInt())
        if(hitType?.name ?: "INVALID" == "INVALID"){
            throw IllegalArgumentException()
        }
        val damage = if(values.size==2 && values[1].matches(Regex("-?\\d+"))) values[1].toInt() else 0
        player.message("${hitType!!.name} hit for $damage")
        player.hit(damage, hitType)
    }
}