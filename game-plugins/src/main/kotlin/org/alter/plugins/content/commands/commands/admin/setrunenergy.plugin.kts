package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */

on_command("setrunenergy", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    player.runEnergy = args[0].toDouble()
    player.sendRunEnergy(args[0].toInt())
}
