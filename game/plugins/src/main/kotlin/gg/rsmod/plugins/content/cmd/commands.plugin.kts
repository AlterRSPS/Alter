package gg.rsmod.plugins.content.cmd

import gg.rsmod.util.console.CLog

object Command {
    fun tryWithUsage(player: Player, args: Array<String>, failMessage: String, tryUnit: Function1<Array<String>, Unit>) {
        try {
            tryUnit.invoke(args)
        } catch (e: Exception) {
            player.message(failMessage)
            e.printStackTrace()
        }
    }
}
