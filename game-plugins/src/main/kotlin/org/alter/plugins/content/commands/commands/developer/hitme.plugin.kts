package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

onCommand("hitme", Privilege.DEV_POWER, description = "Hitsplash by id and amount") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::hitme hitType amount</col>") { values ->
        val hitType = HitType.get(values[0].toInt())
        if (hitType?.name ?: "INVALID" == "INVALID") {
            throw IllegalArgumentException()
        }
        val damage = if (values.size == 2 && values[1].matches(Regex("-?\\d+"))) values[1].toInt() else 0
        player.message("${hitType!!.name} hit for $damage")
        player.hit(damage, hitType)
    }
}
