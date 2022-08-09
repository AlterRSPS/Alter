package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("givebhp", Privilege.DEV_POWER, description = "Give Bountyhunter points"){
    val user = player.username
    val args = player.getCommandArgs()
    val username = args[0]
    val amount = args[1].toInt()
    val target = world.getPlayerForName(username)
    if (target != null) {
        target.addBHP(amount)
        target.message("${user} has given you ${amount} Bounty hunter Points ")
        player.message("${amount} Bounty hunter Points added to ${username}")
    } else {
        player.message("Player ${username} not found")
    }

}
on_command("setbhp", Privilege.DEV_POWER, description = "Set Bountyhunter points"){
    val user = player.username
    val args = player.getCommandArgs()
    val username = args[0]
    val amount = args[1].toInt()
    val target = world.getPlayerForName(username)
    if (target != null) {
        target.setBHP(amount)
        target.message("${user} has set your Bounty hunter Points to ${amount}  ")
        player.message(" ${username} Bounty hunter Points set to ${amount}")
    } else {
        player.message("Player ${username} not found")
    }
}
on_command("checkbhp", description = "Check players Bountyhunter points"){
    val args = player.getCommandArgs()
    val username = args[0]
    val target = world.getPlayerForName(username)
    if (target != null) {
        player.message("${username} has ${target.getBHP()} Bounty hunter Points")
    } else {
        player.message("Player ${username} not found")
    }
}
on_command("bhp", description = "Check your Bountyhunter points"){
    player.message("You have ${player.getBHP()} Bounty hunter Points")
}