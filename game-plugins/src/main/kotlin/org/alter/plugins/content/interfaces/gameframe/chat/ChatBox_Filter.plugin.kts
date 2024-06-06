/**
 * @author CloudS3c
 */
val PARENT_CHAT_BOX_INTERFACE = 162
val GAME_BUTTON_COMPONENT = 7
val PUBLIC_BUTTON_COMPONENT = 11
val PRIVATE_BUTTON_COMPONENT = 15
val CHANNEL_BUTTON_COMPONENT = 19
val CLAN_BUTTON_COMPONENT = 23
val TRADE_BUTTON_COMPONENT = 27
val REPORT_BUG_BUTTON_COMPONENT = 31

on_login {
    player.setVarbit(Varbit.CHATBOX_UNLOCKED, 1)
}

on_button(PARENT_CHAT_BOX_INTERFACE, GAME_BUTTON_COMPONENT) {
    when (player.getInteractingOption()) {
        1 -> {
            player.toggleVarbit(26)
        }
        2 -> {
            player.queue { dialog(this) }
        }
    }
}

suspend fun dialog(it: QueueTask) {
    when (
        it.options(
            "Filter them." /* Filter or unfilter.*/,
            "Do not filter them.",
            title = "Boss kill-counts are not blocked by the spam filter.",
        )
    ) {
        1 -> {
            it.messageBox("Boss kill-count messages that you receive in future will not be blocked by the spam filter.")
        }
        2 -> {
            it.messageBox("CBA For now... Later.")
        }
    }
}

listOf(PRIVATE_BUTTON_COMPONENT, CHANNEL_BUTTON_COMPONENT, CLAN_BUTTON_COMPONENT).forEach {
    on_button(PARENT_CHAT_BOX_INTERFACE, it) {
        player.setVarbit(
            when (it) {
                PRIVATE_BUTTON_COMPONENT -> 13674
                CHANNEL_BUTTON_COMPONENT -> 928
                CLAN_BUTTON_COMPONENT -> 929
                else -> {
                    return@on_button
                }
            },
            when (player.getInteractingOption()) {
                // Option : Varbit Value
                4 -> 2
                3 -> 1
                2 -> 0
                else -> {
                    println("[$PARENT_CHAT_BOX_INTERFACE : $it] ${player.getInteractingOption()} Interacting Option is unknown.")
                    return@on_button
                }
            },
        )
    }
}
listOf(PUBLIC_BUTTON_COMPONENT, TRADE_BUTTON_COMPONENT).forEach {
    on_button(PARENT_CHAT_BOX_INTERFACE, it) {
        // Handled @TODO add setting to log these buttons that are being handled but should not do anything.
    }
}

on_button(PARENT_CHAT_BOX_INTERFACE, REPORT_BUG_BUTTON_COMPONENT) {
    player.runClientScript(2524, -1, -1)
    player.openInterface(553, InterfaceDestination.MAIN_SCREEN)
    player.runClientScript(1104, 1, 1)
}
/**
 * @TODO Needs BUG_REPPORT packet.
 */
on_button(553, 31) {
    player.message("Unhandled yet, will be fixed later.")
}
