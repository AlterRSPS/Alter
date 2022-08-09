package gg.rsmod.plugins.content.skills

import gg.rsmod.game.model.attr.LEVEL_UP_INCREMENT
import gg.rsmod.game.model.attr.LEVEL_UP_SKILL_ID
import kotlin.random.Random


set_level_up_logic {
    val skill = player.attr[LEVEL_UP_SKILL_ID]!!
    val increment = player.attr[LEVEL_UP_INCREMENT]!!

    /*
     * Calculate the combat level for the player if they leveled up a combat
     * skill.
     */
    if (Skills.isCombat(skill)) {
        player.calculateAndSetCombatLevel()
    }

    /*
     * Show the level-up chatbox interface.
     */
    player.queue {
        if(player.getSkills()[skill].currentLevel == 99) {
            player.graphic(1388)
        } else {
            /**
             @TODO Each skill has it's own jingle -> Also diff sound when a player unlocks something new.
             @TODO https://oldschool.runescape.wiki/w/Jingles
             @TODO Also iirc when unlocking a new skill item it shows it on the message.
             */
            var levelupJingles = listOf(29, 67, 50)
            player.playJingle(levelupJingles[Random.nextInt(levelupJingles.size)])
            player.graphic(199, 124)
            player.message("Congratulations, you've just advanced your ${Skills.getSkillName(world, skill)} level. You are now level ${player.getSkills().getBaseLevel(skill)}.")
            world.spawn(AreaSound(player.tile, 2396, 1, 1))
        }
        levelUpMessageBox(skill, increment)
    }
}