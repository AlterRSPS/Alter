package org.alter.plugins.content.areas.lumbridge.spawns

import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository


class ChatSpawnsPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        // Hans
        spawnNpc("npc.hans", x = 3221, z = 3219, direction = Direction.EAST)

        spawnNpc("npc.lumbridge_guide", x = 3238, z = 3220, direction = Direction.WEST)
        spawnNpc("npc.father_aereck", 3243, 3206, 0, 3)
        spawnNpc("npc.hatius_cosaintus", 3233, 3215, 0, 1)
        spawnNpc("npc.ironman_tutor", 3229, 3228, 0, 1)

        // Tutors
        spawnNpc("npc.melee_combat_tutor", 3220, 3238, 0, 1)
        spawnNpc("npc.ranged_combat_tutor", 3218, 3238, 0, 1)
        spawnNpc("npc.magic_combat_tutor", 3216, 3238, 0, 1)
        spawnNpc("npc.woodsman_tutor", 3228, 3246, 0, 1)
        spawnNpc("npc.smithing_apprentice", 3228, 3254, 0, 1)

        spawnNpc("npc.duke_horacio", x = 3212, z = 3220, height = 1, walkRadius = 4, direction = Direction.SOUTH)
        spawnNpc("npc.count_check", 3238, 3199, 0, 0, Direction.NORTH)
        spawnNpc("npc.nigel_8391", 3243, 3201, 0, 2, Direction.WEST)
    }
}
