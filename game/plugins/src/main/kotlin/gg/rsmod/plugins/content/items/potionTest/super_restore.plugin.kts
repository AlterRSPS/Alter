import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.model.priv.PrivilegeSet
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

/**
 *   @Author Cl0ud
 */

// player.inventory.equipment.getBonues()

val restoreList = intArrayOf(Items.SUPER_RESTORE1, Items.SUPER_RESTORE2, Items.SUPER_RESTORE3, Items.SUPER_RESTORE4)

restoreList.forEach {
    on_item_option(it, "Drink") {
        player.playSound(Sounds.DRINKING_POTION)
        for (i in 0 until player.getSkills().maxSkills) {
            //player.getSkills().setBaseLevel(i, 99)
            //val test = player.getSkills().getCurrentLevel()
            // Need to add away to set tempSKills
        }
    }
}

onItemOnNpc(Items.SUPER_RESTORE1, Npcs.MANDY) {
    player.getInteractingNpc()
}
// Movement type
on_command("setrights") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Cba") {values ->
        val privilege = values[0].toInt()
        player.privilege = world.privileges.get(privilege)!!
    }
}