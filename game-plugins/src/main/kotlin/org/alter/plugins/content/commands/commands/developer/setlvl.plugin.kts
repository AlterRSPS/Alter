package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("setlvl", Privilege.DEV_POWER, description = "Set your level") {
    val values = player.getCommandArgs()
    var skill: Int
    try {
        skill = values[0].toInt()
    } catch (e: NumberFormatException) {
        var name = values[0].lowercase()
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
