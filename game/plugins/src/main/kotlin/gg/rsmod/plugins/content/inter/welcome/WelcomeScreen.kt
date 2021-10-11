package gg.rsmod.plugins.content.inter.welcome

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.api.getDisplayComponentId

object WelcomeScreen {
    const val WELCOME_SCREEN_INTERFACE_ID = 378

    fun openWelcomeScreen(player: Player, minSinceLogin: Int, membersDays: Int) {
        player.setVarp(261, minSinceLogin)
        player.setVarp(1780, membersDays)

        player.runClientScript(233, 24772664, 35404, 0, 525, 84, 1901, 0, 800, 8080) // model left
        player.runClientScript(233, 24772665, 35380, 0, 0, 27, 225, 0, 700, 8135) // model right
        player.runClientScript(3092, 2243, "Subscribe Now!")

        player.setComponentText(WELCOME_SCREEN_INTERFACE_ID, 73, "You do not have a Bank PIN.<br>Please visit a bank if you would like one.")

        player.setComponentText(WELCOME_SCREEN_INTERFACE_ID, 7, "<col=2f2fff>Happy Christmas!</col><br>" +
                "<col=003900>Rev193</col> thanks to the fine folks in the <col=ffff00>RsMod</col> Discord<br>" +
                "drop in <col=9f0000>and thank</col> people like <col=ffffff>Tomm, Kris, & Bart</col>.")

        val fullScreenRoot = getDisplayComponentId(DisplayMode.FULLSCREEN)

        player.openOverlayInterface(DisplayMode.FULLSCREEN)
        player.openInterface(fullScreenRoot, 35, WELCOME_SCREEN_INTERFACE_ID)
        player.openInterface(fullScreenRoot, 3, 651)



        player.runClientScript(1080, "https://discord.gg/UznZnZR")
        player.runClientScript(828, 1)

//        player.setComponentHidden(WELCOME_SCREEN_INTERFACE_ID, 51, false) // christmas models



    }
}