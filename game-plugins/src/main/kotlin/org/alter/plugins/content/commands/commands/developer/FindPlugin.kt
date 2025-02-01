package org.alter.plugins.content.commands.commands.developer

import dev.openrune.cache.CacheManager
import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getNpcs
import dev.openrune.cache.CacheManager.getObjects
import dev.openrune.cache.CacheManager.itemSize
import dev.openrune.cache.CacheManager.npcSize
import dev.openrune.cache.CacheManager.objectSize
import gg.rsmod.util.Stopwatch
import java.util.concurrent.TimeUnit
import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class FindPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        listOf("find", "search").forEach { command ->
            onCommand(command, powerRequired = Privilege.DEV_POWER, description = "Search for a keyword trough entity list.") {
                val args = player.getCommandArgs()
                if (args.isEmpty()) {
                    player.message("/find <col=801700>Entity</col>")
                    player.message("For Item: item, i")
                    player.message("For Npc: npc, n")
                    player.message("For Object: object, obj, o")
                } else {
                    val entity = args[0]
                    val keyword = args.copyOfRange(1, args.size).joinToString().replace(",", "")
                    if (keyword.isEmpty()) {
                        player.message("::find $entity [Missing keyword]")
                        return@onCommand
                    }
                    val stopwatch = Stopwatch.createStarted()
                    when (entity) {
                        "item", "i" -> {
                            val itemDefs = (0 until itemSize()).map { CacheManager.getItems().get(it) }.map { it?.id to it?.name }.toTypedArray()
                            val list = search(keyword, itemDefs)
                            list.forEach {
                                var name = it.first!!.replace(keyword, "<col=178000>$keyword</col>", ignoreCase = true)
                                it.second!!.let { second ->
                                    val itemDef = getItem(second)
                                    if (itemDef.isPlaceholder) {
                                        name = name.plus(" (Placeholder)")
                                    }
                                    if (itemDef.noted) {
                                        name = name.plus(" (Noted)")
                                    }
                                }
                                player.message("$name : ${it.second}")
                            }
                            player.message(
                                "Found: ${list.size} results. That include `$keyword` in their name. Search time: ${stopwatch.elapsed(
                                    TimeUnit.MILLISECONDS,
                                )}ms.",
                            )
                        }
                        "object", "o", "obj" -> {
                            val objDefs = (0 until objectSize()).map { getObjects().get(it) }.map { it?.id to it?.name }.toTypedArray()
                            val list = search(keyword, objDefs)
                            list.forEach {
                                val name = it.first!!.replace(keyword, "<col=178000>$keyword</col>", ignoreCase = true)
                                player.message("$name : ${it.second}")
                            }
                            player.message(
                                "Found: ${list.size} results. That include `$keyword` in their name. Search time: ${stopwatch.elapsed(
                                    TimeUnit.MILLISECONDS,
                                )}ms.",
                            )
                        }
                        "npc", "n" -> {
                            val npcDefs = (0 until npcSize()).map { getNpcs().get(it) }.map { it?.id to it?.name }.toTypedArray()
                            val list = search(keyword, npcDefs)
                            list.forEach {
                                val name = it.first!!.replace(keyword, "<col=178000>$keyword</col>", ignoreCase = true)
                                player.message("$name : ${it.second}")
                            }
                            player.message(
                                "Found: ${list.size} results. That include `$keyword` in their name. Search time: ${stopwatch.elapsed(
                                    TimeUnit.MILLISECONDS,
                                )}ms.",
                            )
                        }
                    }
                }
            }
        }
    }

    fun search(
        keyword: String,
        entities: Array<Pair<Int?, String?>>,
    ): List<Pair<String?, Int?>> {
        val list = mutableListOf<Pair<String?, Int?>>()
        entities.forEach { item ->
            item.first?.let {
                if (item.second != null) {
                    if (item.second!!.lowercase().contains(keyword.lowercase())) {
                        list.add(Pair(item.second, item.first))
                    }
                }
            }
        }
        return list
    }
}
