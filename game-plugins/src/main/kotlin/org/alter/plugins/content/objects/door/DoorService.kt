package org.alter.plugins.content.objects.door

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.alter.api.ext.appendToString
import org.alter.game.Server.Companion.logger
import org.alter.game.model.World
import org.alter.game.service.Service
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DoorService : Service {
    val doors = ObjectArrayList<Door>()

    val doubleDoors = ObjectArrayList<DoubleDoorSet>()

    override fun init(
        server: org.alter.game.Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        val singleDoorFile = Paths.get(serviceProperties.get("single-doors") ?: "../data/cfg/doors/single-doors.json")
        val doubleDoorsFile = Paths.get(serviceProperties.get("double-doors") ?: "../data/cfg/doors/double-doors.json")

        Files.newBufferedReader(singleDoorFile).use { reader ->
            val doors = Gson().fromJson<ObjectArrayList<Door>>(reader, object : TypeToken<ObjectArrayList<Door>>() {}.type)
            this.doors.addAll(doors)
        }

        Files.newBufferedReader(doubleDoorsFile).use { reader ->
            val doors = Gson().fromJson<ObjectArrayList<DoubleDoorSet>>(reader, object : TypeToken<ObjectArrayList<DoubleDoorSet>>() {}.type,)
            this.doubleDoors.addAll(doors)
        }

        logger.info { "Loaded ${doors.size.appendToString("single door")} and ${doubleDoors.size.appendToString("double door")}." }
    }
}