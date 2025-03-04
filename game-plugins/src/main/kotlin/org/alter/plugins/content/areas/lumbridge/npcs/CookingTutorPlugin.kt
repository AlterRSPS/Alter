package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class CookingTutorPlugin(
    r: PluginRepository, world: World, server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc("npc.cooking_tutor", x = 3233, z = 3195, walkRadius = 3, height = 0)

        onNpcOption("npc.cooking_tutor", option = "talk-to") {
            player.queue { menu(player) }
        }
    }

    private suspend fun QueueTask.menu(player: Player) {
        when (options(
            player,
            "Can you teach me the basics of cooking please?",
            "Tell me about different food I can make.",
            "Goodbye."
        )) {
            1 -> {
                chatPlayer(player, "Can you teach me the basics of cooking please?", animation = 554)
                chatNpc(player, "The simplest thing to cook is raw meat or fish.", animation = 588)
                chatNpc(
                    player,
                    "Fish can be caught, speak to the fishing tutor south of<br>here in the swamp. Killing cows or chickens will yield<br>raw meat to cook too.",
                    animation = 569,
                )
                itemMessageBox(
                    player,
                    "When you have a full inventory of meat or fish, find a<br>range. Look for this icon on your minimap.",
                    item = "item.null_5090",
                    amountOrZoom = 400,
                )
                chatNpc(
                    player,
                    "You can use your own fire... but it's not as effective<br>and you'll burn more. To build a fire use a tinderbox<br>on logs.",
                    animation = 569,
                )
                chatNpc(
                    player,
                    "Once you've found your range, click on it. This will<br>bring up a menu of the food you can cook.",
                    animation = 589
                )
                itemMessageBox(
                    player,
                    "When you have a full inventory of cooked food, drop<br>the useless burnt food and find a bank. Look for this<br>symbol on your minimap after climbing the stairs of the<br>Lumbridge Castle to the top. There are numerous",
                    item = "item.null_5080",
                    amountOrZoom = 400,
                )
                itemMessageBox(
                    player,
                    "banks around the world, all marked with that symbol.",
                    item = "item.null_5080",
                    amountOrZoom = 400
                )
                chatNpc(
                    player,
                    "If you're interested in quests, I heard my friend the<br>cook in Lumbridge Castle is in need of a hand. Just<br>talk to him and he'll set you off.",
                )
                menu(player)
            }

            2 -> {
                chatPlayer(player, "Tell me about different foods.", animation = 554)
                foodStuffs(player)
            }

            3 -> chatPlayer(player, "Goodbye.", animation = 588)
        }
    }

    private suspend fun QueueTask.foodStuffs(player: Player) {
        when (options(player, "Fish and Meat", "Pies and Pizza", "Go back to teaching")) {
            1 -> {
                doubleItemMessageBox(
                    player,
                    "Fish and meat of most varieties can be cooked very<br>simply on either a fire or range, experiment which one<br>works for you.",
                    item1 = "item.raw_beef",
                    item2 = "item.cooked_meat",
                    amount1 = 400,
                    amount2 = 400,
                )
                foodStuffs(player)
            }

            2 -> {
                doubleItemMessageBox(
                    player,
                    "Use a pot of flour with a bucket of water. You will then<br>get an option to make bread dough, pitta bread dough,<br>pastry dough, or pizza dough. Select pizza or pastry<br>dough.",
                    item1 = "item.bucket_of_water",
                    item2 = "item.pot_of_flour",
                    amount1 = 400,
                    amount2 = 400,
                )
                doubleItemMessageBox(
                    player,
                    "Use the pastry dough with a pie dish then add your<br>filling such as apple or red berries.",
                    item1 = "item.pie_dish",
                    item2 = "item.pastry_dough",
                    amount1 = 400,
                    amount2 = 400,
                )
                chatNpc(
                    player,
                    "Finally cook your pie by using the unbaked pie on a<br>cooking range. Mmmm...pie.",
                    animation = 568
                )
                chatNpc(
                    player,
                    "There's pizza too! Find yourself some tomato and<br>cheese, use on the Pizza dough. Cook the pizza on a<br>range then add any other toppings you want, such as<br>anchovies.",
                    animation = 570,
                )
                foodStuffs(player)
            }

            3 -> menu(player)
        }
    }
}