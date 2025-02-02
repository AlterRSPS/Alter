package org.alter.game.service.game

import dev.openrune.cache.CacheManager.getNpcs
import gg.rsmod.util.ServerProperties
import org.alter.game.Server
import org.alter.game.fs.DefinitionSet
import org.alter.game.model.World
import org.alter.game.service.Service
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcMetadataService : Service {
    private lateinit var path: Path

    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        path = Paths.get(serviceProperties.getOrDefault("path", "../data/cfg/npcs.csv"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        load(world.definitions)
    }

    private fun load(definitions: DefinitionSet) {
        val npcs = getNpcs()

        Files.newBufferedReader(path).use { reader ->
            for (line in reader.lineSequence()) {
                val parts = line.split(",", limit = 2)
                val id = parts.getOrNull(0)?.toIntOrNull() ?: continue
                val examine = parts.getOrNull(1)?.trim()?.removeSurrounding("\"") ?: ""

                npcs[id]?.examine = examine
            }
        }
    }
}
