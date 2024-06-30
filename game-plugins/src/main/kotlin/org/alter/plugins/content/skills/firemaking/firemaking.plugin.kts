package org.alter.plugins.content.skills.firemaking

LogData.values().forEach { log ->
    on_item_on_item(Items.TINDERBOX, log.log) {
        player.queue { Firemaking.burnLog(this, log) }
    }
    on_ground_item_option(log.log, "Light") {
        player.graphic(555)
        player.message("Seems to be working?")
    }
}
