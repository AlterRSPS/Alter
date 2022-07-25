package gg.rsmod.plugins.content.mechanics.npcspawns

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import gg.rsmod.game.Server
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import mu.KLogging
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author CloudS3c
 */
class NpcSpawnService : Service {

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get("./data/cfg/spawns/npc_spawns.yml")
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        val mapper = ObjectMapper(YAMLFactory())
        Files.newBufferedReader(path).use {
            val data = mapper.readValue(it, Array<NpcSpawn>::class.java)
            data.forEach { spawn ->
                world.queue {
                    val npc = Npc(id = spawn.id, Tile(x = spawn.x, z = spawn.z, height = spawn.height), world)
                    npc.walkRadius = spawn.wander
                    when (spawn.facing) {
                        1 -> npc.lastFacingDirection = Direction.NORTH
                        2 -> npc.lastFacingDirection = Direction.EAST
                        3 -> npc.lastFacingDirection = Direction.SOUTH
                        4 -> npc.lastFacingDirection = Direction.WEST
                        else -> {
                            npc.lastFacingDirection = Direction.EAST
                            logger.info("Not found ${spawn.facing} applying default value: EAST")
                        }
                    }
                    npc.respawns
                    world.spawn(npc)

                }
            }
            logger.info {"Loaded ${data.size} Npc Spawns."}
        }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    companion object : KLogging()
}