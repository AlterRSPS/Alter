package org.alter.plugins.content.items.shattered_cane


/**
 * @author CloudS3c 3/12/2023
 * And no this item does not have examine (Same on osrs)
 *
 * Bug: When doing the emote -> Click walk will queue the clicks and execute in the end.
 * Meanwhile on osrs -> It does not get executed only FACE_PAWN if needed when doing the emote.
 */

val required_items =
    arrayOf(
        "item.shattered_hood_t3",
        "item.shattered_top_t3",
        "item.shattered_boots_t3",
        "item.shattered_trousers_t3",
    )

onEquipmentOption("item.shattered_cane", "Skill Emote") {
    if (checkForItems(player)) {
        doEmote(player, 2021)
    }
}
onEquipmentOption("item.shattered_cane", "Boss Emote") {
    if (checkForItems(player)) {
        doEmote(player, 2022)
    }
}
onEquipmentOption("item.shattered_cane", "Quest Emote") {
    if (checkForItems(player)) {
        doEmote(player, 2023)
    }
}
onEquipmentOption("item.shattered_cane", "Fragment Emote") {
    if (checkForItems(player)) {
        player.queue {
            player.lock()
            player.graphic(2020, 92)
            player.animate(8524, 60)
            player.playSound(4215, 100, 8)
            player.playSound(4211, 100, 44)
            player.playSound(4213, 100, 80)
            player.playSound(4212, 100, 104)
            player.unlock()
        }
    }
}

fun doEmote(
    player: Player,
    gfx: Int,
) {
    player.queue {
        player.lock()
        player.graphic(gfx, 0)
        player.animate(9208, 60)
        player.playSound(2344, 100, 116)
        player.playSound(2330, 100, 241)
        player.playSound(2331, 100, 273)
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
