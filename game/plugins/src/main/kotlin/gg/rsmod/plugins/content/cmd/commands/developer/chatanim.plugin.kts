package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin
import gg.rsmod.plugins.content.inter.bank.openBank


on_command("chatanim", Privilege.DEV_POWER, description = "Chat dialogue test") {
    val args = player.getCommandArgs()
    val key = args[0].toInt()
    val npcId = args[1].toInt()

    player.queue {
        chatNpc("Hello World", npcId, animation = key, "hi")
    }
    player.message("$key opened in a dialog", ChatMessageType.ENGINE)
}


