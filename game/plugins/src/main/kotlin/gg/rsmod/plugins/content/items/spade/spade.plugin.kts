package gg.rsmod.plugins.content.items.spade

on_item_option(Items.SPADE, "Dig") {
    player.queue(TaskPriority.STRONG) {
        Dig()
    }
}

suspend fun QueueTask.Dig() {
    wait(2)
    player.animate(Animation.DIG_WITH_SPADE)
    wait(3, 10)
    player.animate(Animation.RESET_CHARACTER)
}