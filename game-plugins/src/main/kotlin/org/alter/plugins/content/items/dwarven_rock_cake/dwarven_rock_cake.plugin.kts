import org.alter.api.cfg.Animation
import org.alter.api.cfg.Sound
import kotlin.math.ceil

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 */

listOf(Items.DWARVEN_ROCK_CAKE, Items.DWARVEN_ROCK_CAKE_7510).forEach {
    on_item_option(it, "Eat") {
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
    on_item_option(it, "Guzzle") {
        player.queue {
            this.player.filterableMessage("You bite hard into the rock cake to guzzle it down.")
            this.player.filterableMessage("OW! A terrible shock jars through your skull.")
            this.player.animate(Animation.CONSUME)
            this.player.playSound(Sound.EAT_ROCKCAKE)
            val incomingDamage = when (player.getCurrentHp()) {
                2 -> 1
                1 -> 0
                else -> ceil(player.getCurrentHp() * 0.10).toInt()
            }
            this.player.hit(incomingDamage)
        }
    }
}
