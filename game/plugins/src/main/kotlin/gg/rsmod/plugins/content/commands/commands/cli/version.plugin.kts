package gg.rsmod.plugins.content.commands.commands.cli;

import gg.rsmod.game.Server.Companion.logger

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */

on_terminal_command("giveadmin") {
    val values = world.getTerminalArgs()
    logger.info("Testing: ${values?.get(0).toString()}")
}
