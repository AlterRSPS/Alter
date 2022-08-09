package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("setlvl", Privilege.DEV_POWER, description = "Set your level") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::setlvl 0 99</col> or <col=801700>::setlvl attack 99</col>") { values ->
        var skill: Int
        try {
            skill = values[0].toInt()
        } catch (e: NumberFormatException) {
            var name = values[0].toLowerCase()
            when (name) {
                "con" -> name = "construction"
                "hp" -> name = "hitpoints"
                "craft" -> name = "crafting"
                "hunt" -> name = "hunter"
                "slay" -> name = "slayer"
                "pray" -> name = "prayer"
                "mage" -> name = "magic"
                "fish" -> name = "fishing"
                "herb" -> name = "herblore"
                "rc" -> name = "runecrafting"
                "fm" -> name = "firemaking"
            }
            skill = Skills.getSkillForName(world, player.getSkills().maxSkills, name)
        }
        if (skill != -1) {
            val level = values[1].toInt()
            player.getSkills().setBaseLevel(skill, level)
        } else {
            player.message("Could not find skill with identifier: ${values[0]}")
        }
    }
}
