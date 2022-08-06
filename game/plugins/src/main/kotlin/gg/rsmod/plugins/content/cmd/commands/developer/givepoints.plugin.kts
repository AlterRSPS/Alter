package gg.rsmod.plugins.content.cmd.commands.developer

on_command("givebhp"){
    val user = player.username
    val args = player.getCommandArgs()
    val text = args[0].toInt()
    player.addBHP(text)
    player.message("${text} Bounty hunter Points given to ${user}")
}
on_command("setbhp"){
    val user = player.username
    val args = player.getCommandArgs()
    val text = args[0].toInt()
    player.setBHP(text)
    player.message("${text} Bounty hunter Points given to ${user}")
}

on_command("bhp"){
    player.message("${player.bountypoints}")
}