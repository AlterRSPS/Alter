package org.alter.plugins.content.items.dwarven_rock_cake

import kotlin.math.ceil
import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Sound
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 */
class DwarvenRockCakePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        listOf("item.dwarven_rock_cake", "item.dwarven_rock_cake_7510").forEach {
            onItemOption(it, "Eat") {
                player.queue {
                    this.player.filterableMessage("Ow! You nearly broke a tooth!")
                    this.player.filterableMessage("The rock cake resists all attempts to eat it.")
                    this.player.animate(Animation.CONSUME)
                    this.player.playSound(Sound.EAT_ROCKCAKE)
                    if (this.player.getCurrentHp() - 1 != 0) {
                        this.player.hit(1)
                    } else {
                        this.player.hit(0)
                    }
                }
            }
            onItemOption(it, "Guzzle") {
                player.queue {
                    this.player.filterableMessage("You bite hard into the rock cake to guzzle it down.")
                    this.player.filterableMessage("OW! A terrible shock jars through your skull.")
                    this.player.animate(Animation.CONSUME)
                    this.player.playSound(Sound.EAT_ROCKCAKE)
                    val incomingDamage =
                        when (player.getCurrentHp()) {
                            2 -> 1
                            1 -> 0
                            else -> ceil(player.getCurrentHp() * 0.10).toInt()
                        }
                    this.player.hit(incomingDamage)
                }
            }
        }
    }
}
