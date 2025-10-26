package org.alter.plugins.content.objects.gates

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.alter.api.ext.appendToString
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GateService : Service {
    val gates = ObjectArrayList<GateSet>()

    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        val file = Paths.get(serviceProperties.get("gates") ?: "../data/cfg/gates/gates.json")
        Files.newBufferedReader(file).use { reader ->
            val gates = Gson().fromJson<ObjectArrayList<GateSet>>(reader, object : TypeToken<ObjectArrayList<GateSet>>() {}.type)
            this.gates.addAll(gates)
        }

        logger.info { "Loaded ${gates.size.appendToString("gate")}." }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}