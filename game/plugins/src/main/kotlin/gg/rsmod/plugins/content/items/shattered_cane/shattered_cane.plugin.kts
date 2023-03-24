package gg.rsmod.plugins.content.items.shattered_cane


/**
 * @author CloudS3c 3/12/2023
 * And no this item does not have examine (Same on osrs)
 *
 * Bug: When doing the emote -> Click walk will queue the clicks and execute in the end.
 * Meanwhile on osrs -> It does not get executed only FACE_PAWN if needed when doing the emote.
 */

val required_items = intArrayOf(
    Items.SHATTERED_HOOD_T3,
    Items.SHATTERED_TOP_T3,
    Items.SHATTERED_BOOTS_T3,
    Items.SHATTERED_TROUSERS_T3
)

on_equipment_option(Items.SHATTERED_CANE, "Skill Emote") {
    if (checkForItems(player)) {
        doEmote(player, 2021)
    }
}
on_equipment_option(Items.SHATTERED_CANE, "Boss Emote") {
    if (checkForItems(player)) {
        doEmote(player, 2022)
    }
}
on_equipment_option(Items.SHATTERED_CANE, "Quest Emote") {
    if (checkForItems(player)) {
        doEmote(player, 2023)
    }
}
on_equipment_option(Items.SHATTERED_CANE, "Fragment Emote") {
    if (checkForItems(player)) {
        player.queue {
            player.lock()
            player.graphic(2020, 92)
            player.animate(8524, 60)
            player.playSound(4215, 100,8)
            player.playSound(4211, 100,44)
            player.playSound(4213, 100,80)
            player.playSound(4212, 100,104)
            player.unlock()
        }
    }
}

fun doEmote(player: Player, gfx: Int) {
    player.queue {
        player.lock()
        player.graphic(gfx, 0)
        player.animate(9208, 60)
        player.playSound(2344, 100,116)
        player.playSound(2330, 100,241)
        player.playSound(2331, 100,273)
        player.unlock()
    }
}

fun checkForItems(p: Player): Boolean {
    if (!p.hasEquipped(required_items)) {
        p.message("You must be wearing the full set of Shattered Relic hunter (T3) to use this emote.")
        return false
    }
    return true
}