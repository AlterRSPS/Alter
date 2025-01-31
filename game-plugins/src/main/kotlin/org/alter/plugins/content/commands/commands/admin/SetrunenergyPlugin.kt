package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.player
import org.alter.api.ext.sendRunEnergy
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */
class SetrunenergyPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("setrunenergy", Privilege.ADMIN_POWER) {
            val args = player.getCommandArgs()
            player.runEnergy = args[0].toDouble()
            player.sendRunEnergy(args[0].toInt())
        }

    }
}
