package org.alter.plugins.content.commands

/**
 * @TODO Rewrite Commands
 */
object Commands_plugin {
    fun tryWithUsage(
        player: Player,
        args: Array<String>,
        failMessage: String,
        tryUnit: Function1<Array<String>, Unit>,
    ) {
        try {
            tryUnit.invoke(args)
        } catch (e: Exception) {
            player.message(failMessage)
            e.printStackTrace()
        }
    }
}