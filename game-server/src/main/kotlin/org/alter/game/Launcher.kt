package org.alter.game

import java.nio.file.Paths

object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("net.rsprot.protocol.internal.networkLogging", "true")
//       System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

        val server = Server()
        server.startServer(apiProps = Paths.get("../data/api.yml"))
        server.startGame(
            filestore = Paths.get("../data", "cache"),
            gameProps = Paths.get("../game.yml"),
            packets = Paths.get("../data", "packets.yml"),
            blocks = Paths.get("../data", "blocks.yml"),
            devProps = Paths.get("../dev-settings.yml"),
        )
    }
}
