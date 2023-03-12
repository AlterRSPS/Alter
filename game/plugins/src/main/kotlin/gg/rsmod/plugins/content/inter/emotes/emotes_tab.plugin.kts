package gg.rsmod.plugins.content.inter.emotes

import gg.rsmod.plugins.content.inter.emotes.EmotesTab.COMPONENT_ID
import gg.rsmod.plugins.content.inter.emotes.EmotesTab.performEmote

on_login {
    player.setInterfaceEvents(interfaceId = COMPONENT_ID, component = 2, range = 0..51, setting = arrayOf(InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp2))
}

on_button(interfaceId = COMPONENT_ID, component = 2) p@ {
    val slot = player.getInteractingSlot()
    val emote = Emote.values.firstOrNull { e -> e.slot == slot } ?: return@p
    performEmote(player, emote)
}