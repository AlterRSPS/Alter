package org.alter.plugins.content.items.essencepouch

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getItems
import org.alter.api.*
import org.alter.api.cfg.*
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
import org.alter.plugins.content.items.other.essencepouch.EssencePouch
import org.alter.rscm.RSCM.getRSCM

class EssencePouchPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        /**
         * The set of essence pouch definitions
         */
        val pouches =
            setOf(
                EssencePouch(id = "item.small_pouch", levelReq = 1, capacity = 3),
                EssencePouch(id = "item.medium_pouch", levelReq = 25, capacity = 6),
                EssencePouch(id = "item.large_pouch", levelReq = 50, capacity = 9),
                EssencePouch(id = "item.giant_pouch", levelReq = 75, capacity = 12),
            )

        /**
         * Bind item option events for the various essence pouches
         */
        pouches.forEach { pouch ->
            onItemOption(item = pouch.id, option = "fill") { fillPouch(player, pouch) }
            onItemOption(item = pouch.id, option = "empty") { emptyPouch(player) }
            onItemOption(item = pouch.id, option = "check") { checkPouch(player) }
        }
    }

/**
 * Handles the filling of an essence pouch. If a pouch is empty, it should attempt to fill the pouch with Pure Essence
 * first, but if none are found then it should default to Rune Essence. If the pouch already contains essence, then it should
 * attempt to fill the pouch with the same type.
 *
 * @param player    The player attempting to fill the pouch
 * @param pouch     The essence pouch definition
 */
fun fillPouch(
    player: Player,
    pouch: EssencePouch,
) {
    if (player.getSkills().getBaseLevel(Skills.RUNECRAFTING) < pouch.levelReq) {
        player.message("This pouch requires level ${pouch.levelReq} ${Skills.getSkillName(world, Skills.RUNECRAFTING)} to use.")
        return
    }

    val item = player.getInteractingItem()
    val containedItem = item.getAttr(ItemAttribute.ATTACHED_ITEM_ID) ?: -1
    val amount = Math.max(item.getAttr(ItemAttribute.ATTACHED_ITEM_COUNT) ?: 0, 0)

    val def = getItems().get(containedItem)
    val inventory = player.inventory
    val freeSpace = pouch.capacity - amount

    if (freeSpace <= 0) {
        player.message("You cannot add any more essence to the pouch.")
        return
    }

    /**
     * Deposits essence from a container, into a pouch
     *
     * @param pouch     The essence pouch receiving the essence
     * @param container The item container to deposit the essence from
     * @param essence   The essence item id
     * @param def       The definition of the essence pouch
     */
    fun deposit(
        pouch: Item,
        container: ItemContainer,
        essence: Int,
        def: EssencePouch,
    ) {
        val fillAmount = Math.min(container.getItemCount(essence), def.capacity)
        val transaction = container.remove(item = essence, amount = fillAmount)
        val amountRemoved = transaction.items.size

        item.putAttr(ItemAttribute.ATTACHED_ITEM_ID, essence)
        pouch.putAttr(ItemAttribute.ATTACHED_ITEM_COUNT, amount + amountRemoved)
    }

    if (def != null) {
        if (!inventory.contains(def.id)) {
            player.message("You can only put ${def.name.lowercase()} in the pouch.")
            return
        }

        deposit(pouch = item, container = player.inventory, essence = def.id, def = pouch)
        return
    }

    if (!inventory.containsAny("item.pure_essence", "item.rune_essence")) {
        player.message("You do not have any essence to fill your pouch with.")
        return
    }

    val essence = if (inventory.contains(getRSCM("item.pure_essence"))) "item.pure_essence" else "item.rune_essence"
    deposit(pouch = item, container = player.inventory, essence = getRSCM(essence), def = pouch)
}

/**
 * Empties the essence contained within the pouch.
 *
 * @param player    The player attempting to empty the pouch contents
 */
fun emptyPouch(player: Player) {
    val pouch = player.getInteractingItem()

    val item = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_ID) ?: -1
    val count = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_COUNT) ?: 0
    val inventory = player.inventory

    if (item != getRSCM("item.rune_essence") && item != getRSCM("item.pure_essence") || count <= 0) {
        player.message("There are no essences in this pouch.")
        return
    }

    val removeCount = Math.min(inventory.freeSlotCount, count)

    if (removeCount <= 0) {
        player.message("You do not have any free space in your inventory.")
        return
    }

    val transaction = inventory.add(item, removeCount)
    val remainder = count - transaction.items.size

    if (remainder == 0) {
        pouch.putAttr(ItemAttribute.ATTACHED_ITEM_ID, -1)
    }

    pouch.putAttr(ItemAttribute.ATTACHED_ITEM_COUNT, remainder)
}

/**
 * Checks the amount of essence in the selected pouch
 *
 * @param player    The player that is checking the pouch contents
 */
fun checkPouch(player: Player) {
    val pouch = player.getInteractingItem()

    val item = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_ID) ?: -1
    val count = pouch.getAttr(ItemAttribute.ATTACHED_ITEM_COUNT) ?: 0

    if (item != getRSCM("item.rune_essence") && item != getRSCM("item.pure_essence") || count <= 0) {
        player.message("There are no essences in this pouch.")
        return
    }

    val name = getItem(item).name.lowercase()

    player.message("There ${count.toLiteral()?.pluralPrefix(count)} ${name.pluralSuffix(count)} in this pouch.")
}

}
