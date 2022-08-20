package gg.rsmod.plugins.content.skills.thieving.objs

import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.bakers_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.crafting_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.crossbow_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.fish_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.food_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.fruit_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.fur_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.gem_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.general_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.magic_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.market_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.scimitar_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.seed_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.silk_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.silver_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.special_gem_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.special_ore_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.spice_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.tea_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.vegetable_stall_steals
import gg.rsmod.util.Misc

enum class Stall(val stalls: IntArray, private val levelRequired: Int, val xp: Double, private val respawnCycles: Int, val attemptMsg: String = "", val steals: Array<StallItem>) {
	//TODO Add support for prifddinas stalls whenever xteas are fixed
    VEGETABLE_STALL(intArrayOf(Objs.VEG_STALL, Objs.VEG_STALL_4708), 2, 10.0, 3, steals = vegetable_stall_steals),
    BAKERS_STALL(intArrayOf(Objs.BAKERS_STALL, Objs.BAKERS_STALL_11730, Objs.BAKERY_STALL_6163), 5, 16.0, 4, steals = bakers_stall_steals),
    TEA_STALL(intArrayOf(Objs.TEA_STALL), 5, 16.0, 5, steals = tea_stall_steals),
    CRAFTING_STALL(intArrayOf(Objs.CRAFTING_STALL, Objs.CRAFTING_STALL_6166), 5, 16.0, 7, steals = crafting_stall_steals),
	GENERAL_STALL(intArrayOf(Objs.GENERAL_STALL), 5, 16.0, 7, steals = general_stall_steals),
	FOOD_STALL(intArrayOf(Objs.FOOD_STALL), 5, 16.0, 7, steals = food_stall_steals),
    SILK_STALL(intArrayOf(Objs.SILK_STALL_11729, Objs.SILK_STALL_36569), 20, 24.0, 6, attemptMsg = "some silk", steals = silk_stall_steals),
    MARKET_STALL(intArrayOf(Objs.MARKET_STALL_14011), 22, 27.0, 10, steals = market_stall_steals),
    FRUIT_STALL(intArrayOf(Objs.FRUIT_STALL_28823), 25, 28.5, 3, attemptMsg = "some fruit", steals = fruit_stall_steals),
    SEED_STALL(intArrayOf(Objs.SEED_STALL_7053), 27, 10.0, 9, steals = seed_stall_steals),
    FUR_STALL(intArrayOf(Objs.FUR_STALL_11732, Objs.FUR_STALL_4278), 35, 36.0, 10, steals = fur_stall_steals),
    FISH_STALL(intArrayOf(Objs.FISH_STALL, Objs.FISH_STALL_4705, Objs.FISH_STALL_4707), 42, 42.0, 10, steals = fish_stall_steals),
    CROSSBOW_STALL(intArrayOf(Objs.CROSSBOW_STALL), 49, 52.0, 9, steals = crossbow_stall_steals),
    SILVER_STALL(intArrayOf(Objs.SILVER_STALL_11734, Objs.SILVER_STALL_6164), 50, 54.0, 20, steals = silver_stall_steals),
	MAGIC_STALL(intArrayOf(Objs.MAGIC_STALL), 65, 100.0, 40, steals = magic_stall_steals),
	SCIMITAR_STALL(intArrayOf(Objs.SCIMITAR_STALL), 65, 160.0, 40, steals = scimitar_stall_steals),
    SPICE_STALL(intArrayOf(Objs.SPICE_STALL_11733), 65, 81.0, 40, steals = spice_stall_steals),
    GEM_STALL(intArrayOf(Objs.GEM_STALL_11731, Objs.GEM_STALL_6162), 75, 160.0, 100, steals = gem_stall_steals),
	ORE_SHOP_COUNTER(intArrayOf(Objs.SHOP_COUNTER_30280), 82, 180.0, 60, steals = special_ore_stall_steals),
	GEM_SHOP_COUNTER(intArrayOf(Objs.SHOP_COUNTER_30279), 75, 160.0, 100, steals = special_gem_stall_steals)
    ;

	fun steal(player: Player, stallId: Int) {
		if (!player.inventory.hasSpace) {
			player.queue {
				messageBox("Your inventory is too full to hold any more.")
			}
			return
		}

		if (player.getSkills()[Skills.THIEVING].currentLevel < levelRequired) {
			player.queue {
				messageBox(
					"You need to be level $levelRequired to steal from the ${
						stallId.getObjName(
							player.world.definitions,
							lowercase = true
						)
					}."
				)
			}
			return
		}

		if (attemptMsg != "") {
			player.filterableMessage(
				"You attempt to steal $attemptMsg from the the ${
					stallId.getObjName(
						player.world.definitions,
						lowercase = true
					)
				}."
			)
		}

		val item = getStolenItem(steals)

		if (player.inventory.add(item).hasSucceeded()) {
			player.queue {
				player.lock()
				wait(1)
				player.animate(832)
				player.playSound(2581)

				val outMsg = "You steal ${
					Misc.getIndefiniteArticle(
						item.getName(
							player.world.definitions,
						).toLowerCase()
					)
				}."
				player.addXp(Skills.THIEVING, xp)

				player.world.queue {
					val obj = player.getInteractingGameObj()
					player.world.remove(obj)
					player.message(outMsg)

					val other = DynamicObject(obj, getEmptyStall(stallId))
					player.world.spawn(other)
					wait(getRespawnTimers(stallId))
					player.world.remove(other)
					player.world.spawn(DynamicObject(obj))
				}
				player.unlock()
			}
		}
	}

	private fun getStolenItem(steals: Array<StallItem>): Item {
		val list = mutableListOf<Item>()
		val roll = Math.random() * 100

		steals.forEach {
			if (it.chance >= roll)
				list.add(Item(it.itemId, it.amount))
		}

		return if (list.isEmpty()) Item(steals.first().itemId) else Item(list.random())
	}

	private fun getEmptyStall(stallId: Int): Int {
		return when (stallId) {
			Objs.BAKERS_STALL -> Objs.EMPTY_STALL
			Objs.FRUIT_STALL_28823 -> Objs.FRUIT_STALL
			Objs.CRAFTING_STALL_6166, Objs.CROSSBOW_STALL, Objs.GEM_STALL_6162, Objs.BAKERY_STALL_6163, Objs.SILVER_STALL_6164 -> Objs.MARKET_STALL_6984
			Objs.SHOP_COUNTER_30279, Objs.SHOP_COUNTER_30280 -> Objs.SHOP_COUNTER_30278
			Objs.CRAFTING_STALL, Objs.FOOD_STALL, Objs.GENERAL_STALL, Objs.MAGIC_STALL, Objs.SCIMITAR_STALL -> Objs.BAMBOO_DESK
			else -> Objs.MARKET_STALL
		}
	}

	private fun getRespawnTimers(stall: Int): Int {
		return when (stall) {
			Objs.BAKERY_STALL_6163 -> respawnCycles * 2
			Objs.SILVER_STALL_6164 -> respawnCycles / 2
			else -> respawnCycles
		}
	}
}

object StallRewards {
	val vegetable_stall_steals = arrayOf(
		StallItem(Items.POTATO, chance = 28.6),
		StallItem(Items.CABBAGE, chance = 20.0),
		StallItem(Items.ONION, chance = 20.0),
		StallItem(Items.TOMATO, chance = 20.0),
		StallItem(Items.GARLIC, chance = 10.0)
	)

	val bakers_stall_steals = arrayOf(
		StallItem(Items.BREAD, chance = 65.0),
		StallItem(Items.CAKE, chance = 25.0),
		StallItem(Items.CHOCOLATE_SLICE, chance = 10.0),
	)

	val silk_stall_steals = arrayOf(
		StallItem(Items.SILK)
	)

	val fruit_stall_steals = arrayOf(
		StallItem(Items.COOKING_APPLE, chance = 40.0),
		StallItem(Items.BANANA, chance = 20.0),
		StallItem(Items.STRAWBERRY, chance = 7.0),
		StallItem(Items.JANGERBERRIES, chance = 5.0),
		StallItem(Items.LEMON, chance = 5.0),
		StallItem(Items.REDBERRIES, chance = 5.0),
		StallItem(Items.PINEAPPLE, chance = 5.0),
		StallItem(Items.LIME, chance = 5.0),
		StallItem(Items.STRANGE_FRUIT, chance = 5.0),
		StallItem(Items.GOLOVANOVA_FRUIT_TOP, chance = 2.0),
		StallItem(Items.PAPAYA_FRUIT, chance = 1.0)
	)

	val market_stall_steals = arrayOf(
		StallItem(Items.JUG, chance = 39.0),
		StallItem(Items.JUG_OF_WATER, chance = 20.0),
		StallItem(Items.GRAPES, chance = 17.0),
		StallItem(Items.JUG_OF_WINE, chance = 13.0),
		StallItem(Items.BOTTLE_OF_WINE, chance = 11.0)
	)

	val seed_stall_steals = arrayOf(
		StallItem(Items.POTATO_SEED_5318, chance = 11.9),
		StallItem(Items.MARIGOLD_SEED, chance = 11.9),
		StallItem(Items.BARLEY_SEED_5305, chance = 11.9),
		StallItem(Items.HAMMERSTONE_SEED_5307, chance = 11.9),
		StallItem(Items.ONION_SEED_5319, chance = 9.01),
		StallItem(Items.ASGARNIAN_SEED_5308, chance = 8.2),
		StallItem(Items.CABBAGE_SEED_5324, chance = 7.14),
		StallItem(Items.YANILLIAN_SEED_5309, chance = 4.65),
		StallItem(Items.ROSEMARY_SEED, chance = 3.64),
		StallItem(Items.NASTURTIUM_SEED, chance = 3.51),
		StallItem(Items.TOMATO_SEED_5322, chance = 3.51),
		StallItem(Items.JUTE_SEED_5306, chance = 3.51),
		StallItem(Items.SWEETCORN_SEED_5320, chance = 3.01),
		StallItem(Items.KRANDORIAN_SEED_5310, chance = 2.4),
		StallItem(Items.STRAWBERRY_SEED_5323, chance = 1.78),
		StallItem(Items.WILDBLOOD_SEED_5311, chance = 1.22),
		StallItem(Items.WATERMELON_SEED_5321, chance = 0.885)
	)

	val spice_stall_steals = arrayOf(
		StallItem(Items.SPICE)
	)

	val fur_stall_steals = arrayOf(
		StallItem(Items.GREY_WOLF_FUR)
	)

	val silver_stall_steals = arrayOf(
		StallItem(Items.SILVER_ORE)
	)

	val gem_stall_steals = arrayOf(
		StallItem(Items.UNCUT_SAPPHIRE, chance = 82.0),
		StallItem(Items.UNCUT_EMERALD, chance = 13.3),
		StallItem(Items.UNCUT_RUBY, chance = 3.91),
		StallItem(Items.UNCUT_DIAMOND, chance = 0.781)
	)

	val fish_stall_steals = arrayOf(
		StallItem(Items.RAW_SALMON, chance = 70.0),
		StallItem(Items.RAW_TUNA, chance = 25.0),
		StallItem(Items.RAW_LOBSTER, chance = 5.0)
	)

	val tea_stall_steals = arrayOf(
		StallItem(Items.CUP_OF_TEA_1978)
	)

	val food_stall_steals = arrayOf(
		StallItem(Items.BANANA)
	)

	val general_stall_steals = arrayOf(
		StallItem(Items.HAMMER),
		StallItem(Items.POT),
		StallItem(Items.TINDERBOX)
	)

	val magic_stall_steals = arrayOf(
		StallItem(Items.AIR_RUNE),
		StallItem(Items.EARTH_RUNE),
		StallItem(Items.FIRE_RUNE)
	)

	val scimitar_stall_steals = arrayOf(
		StallItem(Items.IRON_SCIMITAR),
		StallItem(Items.STEEL_SCIMITAR)
	)

	val crossbow_stall_steals = arrayOf(
		StallItem(Items.BRONZE_BOLTS, amount = 3, chance = 70.0),
		StallItem(Items.BRONZE_LIMBS, chance = 20.0),
		StallItem(Items.WOODEN_STOCK, chance = 0.10)
	)

	val crafting_stall_steals = arrayOf(
		StallItem(Items.CHISEL, chance = 33.3),
		StallItem(Items.NECKLACE_MOULD, chance = 33.3),
		StallItem(Items.RING_MOULD, chance = 33.3)
	)

	val special_ore_stall_steals = arrayOf(
		StallItem(Items.IRON_ORE),
		StallItem(Items.COAL),
		StallItem(Items.SILVER_ORE),
		StallItem(Items.GOLD_ORE),
		StallItem(Items.MITHRIL_ORE),
		StallItem(Items.ADAMANTITE_ORE),
		StallItem(Items.RUNITE_ORE)
	)

	val special_gem_stall_steals = arrayOf(
		StallItem(Items.SAPPHIRE, chance = 33.3),
		StallItem(Items.EMERALD, chance = 20.0),
		StallItem(Items.UNCUT_SAPPHIRE, chance = 16.7),
		StallItem(Items.UNCUT_EMERALD, chance = 10.0),
		StallItem(Items.RUBY, chance = 10.0),
		StallItem(Items.UNCUT_RUBY, chance = 5.0),
		StallItem(Items.DIAMOND, chance = 3.27),
		StallItem(Items.UNCUT_DIAMOND, chance = 1.63),
		StallItem(Items.DRAGONSTONE, chance = 0.0667),
		StallItem(Items.UNCUT_DRAGONSTONE, chance = 0.0333),
		StallItem(Items.ONYX, chance = 0.002)
	)
}