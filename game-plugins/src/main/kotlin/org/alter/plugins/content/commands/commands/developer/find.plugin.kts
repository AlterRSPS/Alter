import com.google.common.base.Stopwatch
import org.alter.game.model.priv.Privilege
import java.util.concurrent.TimeUnit

listOf("find", "search").forEach { command ->
    on_command(command, powerRequired = Privilege.DEV_POWER, description = "Search for a keyword trough entity list.") {
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
                return@on_command
            }
            val stopwatch = Stopwatch.createStarted()
            when (entity) {
                "item", "i" -> {
                    val itemDefs = (0 until getDefCount(ItemDef::class)).map { world.definitions.getNullable(ItemDef::class.java, it) }.map { it?.id to it?.name }.toTypedArray()
                    val list = search(keyword, itemDefs)
                    list.forEach {
                        var name = it.first!!.replace(keyword, "<col=178000>$keyword</col>", ignoreCase = true)
                        it.second!!.let { second ->
                            val itemDef = getDefs(ItemDef::class, second)
                            if (itemDef.isPlaceholder) {
                                name = name.plus(" (Placeholder)")
                            }
                            if (itemDef.noted) {
                                name = name.plus(" (Noted)")
                            }
                        }
                        player.message("$name : ${it.second}")
                    }
                    player.message("Found: ${list.size} results. That include `$keyword` in their name. Search time: ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")
                }
                "object", "o", "obj" -> {
                    val objDefs = (0 until getDefCount(ObjectDef::class)).map { world.definitions.getNullable(ObjectDef::class.java, it) }.map { it?.id to it?.name }.toTypedArray()
                    val list = search(keyword, objDefs)
                    list.forEach {
                        val name = it.first!!.replace(keyword, "<col=178000>$keyword</col>", ignoreCase = true)
                        player.message("$name : ${it.second}")
                    }
                    player.message("Found: ${list.size} results. That include `$keyword` in their name. Search time: ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")
                }
                "npc", "n" -> {
                    val npcDefs = (0 until getDefCount(NpcDef::class)).map { world.definitions.getNullable(NpcDef::class.java, it) }.map { it?.id to it?.name }.toTypedArray()
                    val list = search(keyword, npcDefs)
                    list.forEach {
                        val name = it.first!!.replace(keyword, "<col=178000>$keyword</col>", ignoreCase = true)
                        player.message("$name : ${it.second}")
                    }
                    player.message("Found: ${list.size} results. That include `$keyword` in their name. Search time: ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")
                }
            }
        }
    }
}

fun search(
        keyword: String,
        entities: Array<Pair<Int?, String?>>
    ) : List<Pair<String?, Int?>> {
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

