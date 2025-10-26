package org.alter.plugins.content.areas.lumbridge.spawns

import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

/**Example
 *spawnNpc(npc = "npc.ID", x = xxxx, y = zzzz, height = 0, walk = 0, direction = Direction.NORTH)
 */


class SpawnPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        spawnNpc(npc = "npc.man_3106", x = 3206, z = 3219, walkRadius = 20, height = 1, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.man_3106", x = 3216, z = 3219, walkRadius = 20, direction = Direction.EAST)
        spawnNpc(npc = "npc.man_3106", x = 3207, z = 3227, walkRadius = 20, direction = Direction.EAST)
        spawnNpc(npc = "npc.man_3108", x = 3209, z = 3215, walkRadius = 20, height = 1, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.man_3108", x = 3221, z = 3219, walkRadius = 20, direction = Direction.EAST)
        spawnNpc(npc = "npc.woman_3111", x = 3211, z = 3213, walkRadius = 20, height = 1, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.woman_3111", x = 3217, z = 3205, walkRadius = 20, direction = Direction.NORTH)
        spawnNpc(npc = "npc.rat_2854", x = 3207, z = 3202, walkRadius = 10, direction = Direction.NORTH)
        spawnNpc(npc = "npc.rat_2854", x = 3205, z = 3204, walkRadius = 10, direction = Direction.NORTH)
        spawnNpc(npc = "npc.rat_2854", x = 3206, z = 3202, walkRadius = 10, direction = Direction.NORTH)
        spawnNpc(npc = "npc.rat_2854", x = 3207, z = 3203, walkRadius = 10, direction = Direction.NORTH)
        spawnNpc(npc = "npc.rat_2854", x = 3205, z = 3209, walkRadius = 10, direction = Direction.EAST)
        spawnNpc(npc = "npc.rat_2854", x = 3207, z = 3209, walkRadius = 10, direction = Direction.EAST)
        spawnNpc(npc = "npc.imp_5007", x = 3217, z = 3226, walkRadius = 20, direction = Direction.EAST)
        spawnNpc(npc = "npc.sheep_2789", x = 3196, z = 3263, walkRadius = 10, direction = Direction.NORTH)
        spawnNpc(npc = "npc.sheep_2789", x = 3199, z = 3261, walkRadius = 10, direction = Direction.EAST)
        spawnNpc(npc = "npc.sheep_2789", x = 3201, z = 3272, walkRadius = 10, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.sheep_2789", x = 3202, z = 3268, walkRadius = 10, direction = Direction.NORTH)
        spawnNpc(npc = "npc.sheep_2789", x = 3206, z = 3266, walkRadius = 10, direction = Direction.WEST)
        spawnNpc(npc = "npc.ram_1265", x = 3201, z = 3263, walkRadius = 10, direction = Direction.NORTH)
        spawnNpc(npc = "npc.ram_1265", x = 3207, z = 3271, walkRadius = 10, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.ram_1265", x = 3195, z = 3271, walkRadius = 10, direction = Direction.EAST)
        spawnNpc(npc = "npc.huge_spider_134", x = 3168, z = 3243, walkRadius = 7, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.giant_spider", x = 3165, z = 3251, walkRadius = 7, direction = Direction.EAST)
        spawnNpc(npc = "npc.giant_spider_3018", x = 3246, z = 3248, walkRadius = 7, direction = Direction.EAST)
        spawnNpc(npc = "npc.giant_spider_3017", x = 3241, z = 3245, walkRadius = 7, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.giant_spider_3017", x = 3253, z = 3243, walkRadius = 7, direction = Direction.WEST)
        spawnNpc(npc = "npc.giant_spider_3017", x = 3245, z = 3235, walkRadius = 7, direction = Direction.NORTH)
        spawnNpc(npc = "npc.giant_spider_3017", x = 3253, z = 3234, walkRadius = 7, direction = Direction.WEST)
        spawnNpc(npc = "npc.goblin_3028", x = 3264, z = 3232, walkRadius = 8, direction = Direction.WEST)
        spawnNpc(npc = "npc.goblin_2246", x = 3247, z = 3244, walkRadius = 8, direction = Direction.NORTH)
        spawnNpc(npc = "npc.goblin_2248", x = 3244, z = 3244, walkRadius = 8, direction = Direction.NORTH)
        spawnNpc(npc = "npc.goblin_2484", x = 3241, z = 3242, walkRadius = 8, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.goblin_3028", x = 3253, z = 3245, walkRadius = 8, direction = Direction.WEST)
        spawnNpc(npc = "npc.goblin_3039", x = 3255, z = 3236, walkRadius = 8, direction = Direction.WEST)
        spawnNpc(npc = "npc.goblin_3054", x = 3256, z = 3230, walkRadius = 8, direction = Direction.WEST)
        spawnNpc(npc = "npc.goblin_3028", x = 3221, z = 3271, walkRadius = 8, direction = Direction.WEST)
        // TODO: Add more goblin spawns
        spawnNpc(npc = "npc.drunken_man", x = 3230, z = 3241, walkRadius = 3, direction = Direction.EAST)
        spawnNpc(npc = "npc.man_3109", x = 3228, z = 3239, walkRadius = 3, direction = Direction.WEST)
        spawnNpc(npc = "npc.woman_3112", x = 3229, z = 3238, walkRadius = 3, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.man_3014", x = 3231, z = 3236, walkRadius = 3, direction = Direction.WEST)
        spawnNpc(npc = "npc.zombie_rat_3970", x = 3246, z = 3198, walkRadius = 5, direction = Direction.WEST)
        spawnNpc(npc = "npc.zombie_rat", x = 3239, z = 3198, walkRadius = 5, direction = Direction.WEST)

        // Item spawns
        spawnItem(item = "item.logs", amount = 1, x = 3205, z = 3224, height = 2)
        spawnItem(item = "item.logs", amount = 1, x = 3205, z = 3226, height = 2)
        spawnItem(item = "item.logs", amount = 1, x = 3208, z = 3225, height = 2)
        spawnItem(item = "item.logs", amount = 1, x = 3209, z = 3224, height = 2)
        spawnItem(item = "item.mind_rune", amount = 1, x = 3206, z = 3208)
        spawnItem(item = "item.bronze_arrow", amount = 1, x = 3205, z = 3227)
        spawnItem(item = "item.bronze_dagger", amount = 1, x = 3213, z = 3216, height = 1)
        spawnItem(item = "item.knife", amount = 1, x = 3205, z = 3212)
        spawnItem(item = "item.knife", amount = 1, x = 3224, z = 3202)
        spawnItem(item = "item.pot", amount = 1, x = 3209, z = 3214)
        spawnItem(item = "item.bowl", amount = 1, x = 3208, z = 3214)
        spawnItem(item = "item.jug", amount = 1, x = 3211, z = 3212)

        spawnObj(obj = "object.altar_409", x = 3222, z = 3215, rot = 6)
    }
}
