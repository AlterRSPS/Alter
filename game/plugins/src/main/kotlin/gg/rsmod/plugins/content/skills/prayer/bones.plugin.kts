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
    on_item_on_obj(obj = Objs.CHAOS_ALTAR_411, item = bones.id) {
        if (!Offer.canOffer(player, bones)) {
            return@on_item_on_obj
        }
        if (player.inventory.contains(bones.id)) {
            // 1(gilded), 2(ecto), 3(chaos)
            Offer.OfferBones(player, bones, 3)
        }
    }
}