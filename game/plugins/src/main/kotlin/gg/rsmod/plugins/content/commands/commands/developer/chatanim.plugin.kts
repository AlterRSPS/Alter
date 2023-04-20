package gg.rsmod.plugins.content.commands.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.commands.Commands_plugin
import gg.rsmod.plugins.content.interfaces.bank.openBank


on_command("chatanim", Privilege.DEV_POWER, description = "Chat dialogue test") {
    val args = player.getCommandArgs()
    val key = args[0].toInt()
    val npcId = args[1].toInt()

    player.queue {
        chatNpc("Hello World", npcId, animation = key, "hi")
    }
    player.message("$key opened in a dialog", ChatMessageType.ENGINE)
}


