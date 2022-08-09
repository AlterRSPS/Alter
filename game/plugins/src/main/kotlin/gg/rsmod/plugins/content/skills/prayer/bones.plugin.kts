package gg.rsmod.plugins.content.skills.prayer



Bones.values.forEach { bones ->
    on_item_option(item = bones.id, option = "bury"){
        if (!Bury.canBury(player, bones)) {
            return@on_item_option
        }
        val inventorySlot = player.getInteractingItemSlot()
        if (player.inventory.remove(item = bones.id, beginSlot = inventorySlot).hasSucceeded()) {
            Bury.Bury(player, bones)

        }
    }
    /**not working.
    on_item_on_obj(obj = Objs.CHAOS_ALTAR_411, item = bones.id) {
        player.message("You offer the to Chaos altar")
        if (!Offer.canOffer(player, bones)) {
            return@item_on_obj
        }
        val inventorySlot = player.getInteractingItemSlot()
        if (player.inventory.remove(item = bones.id, beginSlot = inventorySlot).hasSucceeded()) {
            // 1(gilded), 2(ecto), 3(chaos)
            Offer.OfferBones(player, bones, 3)

        }
    }**/
}