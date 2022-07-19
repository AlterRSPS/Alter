package gg.rsmod.plugins.content.skills.firemaking

LogData.values().forEach { log ->
    on_item_on_item(Items.TINDERBOX, log.log) {
        player.queue { Firemaking.burnLog(this, log) }
    }
}
