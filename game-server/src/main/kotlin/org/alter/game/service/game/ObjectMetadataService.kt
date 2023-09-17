package org.alter.game.service.game

import org.alter.game.Server
import org.alter.game.fs.DefinitionSet
import org.alter.game.fs.def.ObjectDef
import org.alter.game.model.World
import org.alter.game.service.Service
import gg.rsmod.util.ServerProperties
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectMetadataService : Service {

    private lateinit var path: Path

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        path = Paths.get(serviceProperties.getOrDefault("path", "../data/cfg/locs.csv"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        load(world.definitions)
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun load(definitions: DefinitionSet) {
        path.toFile().forEachLine { line ->
            val parts = line.split(',')
            if (parts.size >= 2) {
                val id = parts[0].toIntOrNull()
                val examine = line.substringAfter(',').trim()
                if (id != null) {
                    val def = definitions.getNullable(ObjectDef::class.java, id) ?: return@forEachLine
                    def.examine = examine.replace("\"", "")
                }
            }
        }
    }
}