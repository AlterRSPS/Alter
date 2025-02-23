package org.alter.plugins.content.areas.lumbridge.npcs.stores

import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.shop.PurchasePolicy
import org.alter.game.model.shop.ShopItem
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.plugins.content.mechanics.shops.CoinCurrency
import org.alter.rscm.RSCM.getRSCM


class BobPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    private val dialogOptions: List<String> = listOf(
        "Give me a quest!",
        "Have you anything to sell?",
        "Can you repair my items for me?",
    )

    private val storeItems = listOf(
        ShopItem(getRSCM("item.bronze_pickaxe"), 5, 1, 0),
        ShopItem(getRSCM("item.bronze_axe"), 10, 16, 9),
        ShopItem(getRSCM("item.iron_axe"), 5, 56, 33),
        ShopItem(getRSCM("item.steel_axe"), 3, 200, 120),
        ShopItem(getRSCM("item.iron_battleaxe"), 5, 182, 109),
        ShopItem(getRSCM("item.steel_battleaxe"), 2, 650, 390),
        ShopItem(getRSCM("item.mithril_battleaxe"), 1, 1690, 1014),
    )

    init {
        spawnNpc("npc.bob_10619", 3230, 3203, 0, 2, Direction.EAST)

        createShop("Bob's Brilliant Axes.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
            storeItems.forEachIndexed { index, item ->
                items[index] = item
            }
        }

        onNpcOption("npc.bob_10619", option = "talk-to")
        {
            player.queue { dialog(player) }
        }

        onNpcOption("npc.bob_10619", option = "trade")
        {
            player.shop()
        }
    }

    fun Player.shop() = this.openShop("Bob's Brilliant Axes.")

    suspend fun QueueTask.dialog(player: Player) {
        when (options(player, *dialogOptions.toTypedArray())) {
            1 -> {
                chatPlayer(player, "Give me a quest!")
                chatNpc(player, "Get yer own!")
            }

            2 -> player.shop()

            3 -> {
                chatPlayer(player, "Can you repair my items for me?")
                chatNpc(player, "Of course I'll repair it, though the materials may cost you. Just hand me the item and I'll take a look.")
            }
        }
    }
}
