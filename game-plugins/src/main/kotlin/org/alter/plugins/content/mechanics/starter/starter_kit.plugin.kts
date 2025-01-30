package org.alter.plugins.content.mechanics.starter

import org.alter.game.model.attr.NEW_ACCOUNT_ATTR
import org.alter.rscm.RSCM.getRSCM

onLogin {
    if (player.attr[NEW_ACCOUNT_ATTR] ?: return@onLogin) {
        with(player.inventory) {
            add(getRSCM("item.logs"), 5)
            add(getRSCM("item.tinderbox"))
            add(getRSCM("item.bread"), 5)
            add(getRSCM("item.bronze_pickaxe"))
            add(getRSCM("item.bronze_dagger"))
            add(getRSCM("item.knife"))
        }
    }
}
