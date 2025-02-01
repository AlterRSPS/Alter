package org.alter.plugins.content.commands.commands.developer

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class HitmePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("hitme", Privilege.DEV_POWER, description = "Hitsplash by id and amount") {
            val values = player.getCommandArgs()
            val hitType = HitType.get(values[0].toInt())
            if (hitType?.name ?: "INVALID" == "INVALID") {
                throw IllegalArgumentException()
            }
            val damage = if (values.size == 2 && values[1].matches(Regex("-?\\d+"))) values[1].toInt() else 0
            player.message("${hitType!!.name} hit for $damage")
            player.hit(damage, hitType)
        }
    }
}
