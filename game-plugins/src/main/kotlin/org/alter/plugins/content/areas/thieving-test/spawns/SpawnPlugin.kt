package org.alter.plugins.content.areas.`thieving-test`.spawns

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
        spawnNpc(npc = "npc.man_3106", x = 2591, z = 4730, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.farmer_3114", x = 2586, z = 4730, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.ham_member", x = 2582, z = 4730, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.al_kharid_warrior", x = 2591, z = 4727, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.rogue_526", x = 2586, z = 4727, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.cave_goblin_2268", x = 2582, z = 4727, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.master_farmer_5730", x = 2591, z = 4724, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.guard_397", x = 2586, z = 4724, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.menaphite_thug_3550", x = 2582, z = 4724, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.knight_of_ardougne", x = 2591, z = 4721, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.paladin_3293", x = 2586, z = 4721, walkRadius = 2, direction = Direction.SOUTH)
        spawnNpc(npc = "npc.hero_3295", x = 2582, z = 4721, walkRadius = 2, direction = Direction.SOUTH)

        spawnObj(obj = "object.veg_stall", x = 2580, z = 4735, rot = 2)
        spawnObj(obj = "object.bakers_stall_11730", x = 2582, z = 4735, rot = 2)
        spawnObj(obj = "object.tea_stall", x = 2584, z = 4735, rot = 2)
        spawnObj(obj = "object.silk_stall_11729", x = 2586, z = 4735, rot = 2)
        spawnObj(obj = "object.market_stall_14011", x = 2588, z = 4735, rot = 2)
        spawnObj(obj = "object.seed_stall_7053", x = 2590, z = 4735, rot = 2)
        spawnObj(obj = "object.fur_stall_4278", x = 2592, z = 4735, rot = 2)
        spawnObj(obj = "object.fish_stall", x = 2594, z = 4735, rot = 2)
        spawnObj(obj = "object.silver_stall_11734", x = 2596, z = 4733, rot = 3)
        spawnObj(obj = "object.spice_stall_11733", x = 2596, z = 4731, rot = 3)
        spawnObj(obj = "object.gem_stall_11731", x = 2596, z = 4729, rot = 3)

        spawnObj(obj = "object.chest_11735", x = 2596, z = 4724, rot = 3)
        spawnObj(obj = "object.chest_11736", x = 2596, z = 4722, rot = 3)
        spawnObj(obj = "object.chest_11737", x = 2596, z = 4720, rot = 3)
        spawnObj(obj = "object.chest_11738", x = 2596, z = 4718, rot = 3)
        spawnObj(obj = "object.chest_11739", x = 2596, z = 4716, rot = 3)
        spawnObj(obj = "object.chest_11740", x = 2596, z = 4714, rot = 3)
        spawnObj(obj = "object.chest_11741", x = 2596, z = 4712, rot = 3)
    }
}
