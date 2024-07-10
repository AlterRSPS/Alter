package org.alter.plugins.service.marketvalue

import dev.openrune.cache.CacheManager.getItems
import dev.openrune.cache.CacheManager.itemSize
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemMarketValueService : Service {
    private val values = Int2IntOpenHashMap()

    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        val items = itemSize()
        for (i in 0 until items) {
            val def = getItems().get(i) ?: continue

            if (!def.noted && def.name.isNotBlank()) {
                values[i] = def.cost
                if (def.noteTemplateId == 0 && def.noteLinkId > 0) {
                    values[def.noteLinkId] = def.cost
                }
            }
        }
        logger.info { "Loaded ${values.size} item values." }
    }

    fun get(item: Int): Int {
        val value = values[item]
        if (value != values.defaultReturnValue()) {
            return value
        }
        return 0
    }

    private val logger = KotlinLogging.logger {}
}
