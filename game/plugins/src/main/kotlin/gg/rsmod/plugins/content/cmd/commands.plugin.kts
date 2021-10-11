package gg.rsmod.plugins.content.cmd

import gg.rsmod.game.message.impl.UpdateInvFullMessage
import gg.rsmod.game.model.attr.NO_CLIP_ATTR
import gg.rsmod.game.model.bits.INFINITE_VARS_STORAGE
import gg.rsmod.game.model.bits.InfiniteVarsType
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.model.timer.FORCE_DISCONNECTION_TIMER
import gg.rsmod.game.service.game.ItemMetadataService
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage
import gg.rsmod.plugins.content.combat.CombatConfigs
import gg.rsmod.plugins.content.combat.formula.MagicCombatFormula
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.content.combat.getCombatTarget
import gg.rsmod.plugins.content.magic.MagicSpells
import gg.rsmod.plugins.content.skills.herblore.Herbs
import java.text.DecimalFormat
import kotlin.system.exitProcess

on_commands(Privilege.ADMIN_POWER, "Lmao", "Lol") {
    player.message("Dis works.")
}

on_command("reloaditems") {
    load_service(ItemMetadataService())
    player.message("ItemMetaDataService was reloaded.")
}

on_command("emptybank") {
    player.bank.removeAll()
}

on_command("cmds", Privilege.ADMIN_POWER) {
    val command = get_all_commands().joinToString()
    val messages = ArrayList<String>()
    var buf = ""
    val split = command.split(", ")
    split.forEach { s ->
        buf += s
        buf += ", "
        if (buf.length > 75) {
            messages.add(buf)
            buf = ""
        }
    }
    if(buf != "") {
        buf = buf.substring(0, buf.length-2)
        messages.add(buf)
    }
    player.message("Commands:")

    messages.forEach {
            s -> player.message(s)
    }
}

on_command("max") {
    val target = player.getCombatTarget() ?: player
    CombatClass.values.forEach { combatClass ->
        val max: Int
        val accuracy: Double
        when (combatClass) {
            CombatClass.MAGIC -> {
                accuracy = MagicCombatFormula.getAccuracy(player, target)
                max = MagicCombatFormula.getMaxHit(player, target)
            }
            CombatClass.RANGED -> {
                accuracy = RangedCombatFormula.getAccuracy(player, target)
                max = RangedCombatFormula.getMaxHit(player, target)
            }
            CombatClass.MELEE -> {
                accuracy = MeleeCombatFormula.getAccuracy(player, target)
                max = MeleeCombatFormula.getMaxHit(player, target)
            }
            else -> throw IllegalStateException("Unhandled combat class: ${CombatConfigs.getCombatClass(player)} for $player")
        }
        val name = combatClass.name.toLowerCase().capitalize()
        val message = """<col=178000>$name:</col>   Max hit=<col=801700>$max</col>   Accuracy=<col=801700>${(accuracy * 100).toInt()}%</col> [${String.format("%.6f", accuracy)}]"""
        player.message(message)
    }
}

on_command("empty") {
    player.inventory.removeAll()
}

on_command("shutdown", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::shutdown 500</col>"){ values ->
        val cycles = values[0].toInt()
        world.queue {
            world.rebootTimer = cycles
            world.sendRebootTimer(cycles)
            wait(cycles)
            world.players.forEach { player -> player.timers[FORCE_DISCONNECTION_TIMER] = 0 }
            wait(5)
            exitProcess(0)
        }
    }
}

on_command("reboot", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::reboot 500</col>") { values ->
        val cycles = values[0].toInt()
        world.rebootTimer = cycles
        world.sendRebootTimer()
    }
}

on_command("home", Privilege.ADMIN_POWER) {
    val home = world.gameContext.home
    player.moveTo(home)
}

on_command("noclip", Privilege.ADMIN_POWER) {
    val noClip = !(player.attr[NO_CLIP_ATTR] ?: false)
    player.attr[NO_CLIP_ATTR] = noClip
    player.message("No-clip: ${if (noClip) "<col=178000>enabled</col>" else "<col=801700>disabled</col>"}")
}

on_command("mypos", Privilege.ADMIN_POWER) {
    val instancedMap = world.instanceAllocator.getMap(player.tile)
    val tile = player.tile
    if (instancedMap == null) {
        player.message("Tile=[<col=801700>${tile.x}, ${tile.z}, ${tile.height}</col>], Region=${player.tile.regionId}")
    } else {
        val delta = tile - instancedMap.area.bottomLeft
        player.message("Tile=[<col=801700>${tile.x}, ${tile.z}, ${tile.height}</col>], Relative=[${delta.x}, ${delta.z}]")
    }
}

on_command("tele", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::tele 3200 3200</col>") { values ->
        val x = values[0].toInt()
        val z = values[1].toInt()
        val height = if (values.size > 2) values[2].toInt() else 0
        player.moveTo(x, z, height)
    }
}

on_command("teler", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::teler 12850</col>") { values ->
        val region = values[0].toInt()
        val tile = Tile.fromRegion(region)
        player.moveTo(tile)
    }
}

on_command("anim", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::anim 1</col>") { values ->
        val id = values[0].toInt()
        player.animate(id)
        player.message("Animate: $id")
    }
}

on_command("gfx", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::gfx 1</col>") { values ->
        val id = values[0].toInt()
        val height = if (values.size >= 2) values[1].toInt() else 100
        player.graphic(id, height)
        player.message("Graphic: $id")
    }
}

on_command("sound", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::sound 1</col>") { values ->
        val id = values[0].toInt()
        player.playSound(id)
        player.message("Sound: $id")
    }
}

on_command("song", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::song 1</col>") { values ->
        val id = values[0].toInt()
        player.playSong(id)
        player.message("Song: $id")
    }
}

on_command("jingle", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::jingle 1</col>") { values ->
        val id = values[0].toInt()
        player.playJingle(id)
        player.message("Jingle: $id")
    }
}

on_command("infrun", Privilege.ADMIN_POWER) {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)
    player.message("Infinite run: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("infpray", Privilege.ADMIN_POWER) {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.PRAY)
    player.message("Infinite prayer: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.PRAY)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("infhp", Privilege.ADMIN_POWER) {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.HP)
    player.message("Infinite hp: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.HP)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("infrunes", Privilege.ADMIN_POWER) {
    player.toggleVarbit(MagicSpells.INF_RUNES_VARBIT)
    player.message("Infinite runes: ${if (player.getVarbit(MagicSpells.INF_RUNES_VARBIT) != 1) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("invisible", Privilege.ADMIN_POWER) {
    player.invisible = !player.invisible
    player.message("Invisible: ${if (!player.invisible) "<col=801700>false</col>" else "<col=178000>true</col>"}")
}

on_command("npc", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::npc 1</col>") { values ->
        val id = values[0].toInt()
        val npc = Npc(id, player.tile, world)
        player.message("NPC: $id , on x:${player.tile.x} z:${player.tile.z}");
        world.spawn(npc)
    }
}

on_command("removenpc", Privilege.ADMIN_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val npc = chunk.getEntities<Npc>(player.tile, EntityType.NPC).firstOrNull()
    if (npc != null) {
        world.remove(npc)
    } else {
        player.message("No NPC found in tile.")
    }
}

on_command("obj", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::obj 1</col>") { values ->
        val id = values[0].toInt()
        val type = if (values.size > 1) values[1].toInt() else 10
        val rot = if (values.size > 2) values[2].toInt() else 0
        val obj = DynamicObject(id, type, rot, player.tile)
        world.spawn(obj)
    }
}

on_command("removeobj", Privilege.ADMIN_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val obj = chunk.getEntities<GameObject>(player.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
    if (obj != null) {
        world.remove(obj)
    } else {
        player.message("No object found in tile.")
    }
}

on_command("aboutobj", Privilege.ADMIN_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val obj = chunk.getEntities<GameObject>(player.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
    if (obj != null) {
        player.message("obj [id = ${obj.id}, type = ${obj.type}, rot = ${obj.rot}]")
    } else {
        player.message("No object found in tile.")
    }
}

on_command("master", Privilege.ADMIN_POWER) {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, 99)
    }
    player.calculateAndSetCombatLevel()
}

on_command("reset", Privilege.ADMIN_POWER) {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, if (i == Skills.HITPOINTS) 10 else 1)
    }
    player.calculateAndSetCombatLevel()
}

on_command("setlvl", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::setlvl 0 99</col> or <col=801700>::setlvl attack 99</col>") { values ->
        var skill: Int
        try {
            skill = values[0].toInt()
        } catch (e: NumberFormatException) {
            var name = values[0].toLowerCase()
            when (name) {
                "con" -> name = "construction"
                "hp" -> name = "hitpoints"
                "craft" -> name = "crafting"
                "hunt" -> name = "hunter"
                "slay" -> name = "slayer"
                "pray" -> name = "prayer"
                "mage" -> name = "magic"
                "fish" -> name = "fishing"
                "herb" -> name = "herblore"
                "rc" -> name = "runecrafting"
                "fm" -> name = "firemaking"
            }
            skill = Skills.getSkillForName(world, player.getSkills().maxSkills, name)
        }
        if (skill != -1) {
            val level = values[1].toInt()
            player.getSkills().setBaseLevel(skill, level)
        } else {
            player.message("Could not find skill with identifier: ${values[0]}")
        }
    }
}

on_command("getid") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example for proper command <col=801700>::getid Item/Npc/Object Keyword </col>") { values ->

    }
}

on_command("getitems", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::itemsn hat </col>") { values ->
        var items_list = mutableListOf<Int>()
        var item_name = values[0].toString() // Search trough and if it matches in examine / name add to array and spawn all to bank | $ For spaces

        for (i in 0 until world.definitions.getCount(ItemDef::class.java)) {
            val def = world.definitions.get(ItemDef::class.java, Item(i).toUnnoted(world.definitions).id)
            val items_name = def.name?.toLowerCase()
            val items_examine = def.examine?.toLowerCase()
            if (!def.isPlaceholder && items_name != "null") {
                if (items_name.contains(item_name, ignoreCase = true)) { // <- Why doesnt this one need a null check? D:
                    items_list.add(def.id)
                } else if (items_examine != null) {
                    if (items_examine.contains(item_name, ignoreCase = true)) {
                        items_list.add(def.id)
                    }
                }
            }
        }
            for (i in 0 until items_list.count()) {
                player.bank.add(items_list[i], 10)
            }
        player.message("Total Count: ${items_list.count()} with keyword: $item_name in their name and examine")
    }
}
on_command("getitemstype", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::itemsn hat </col>") { values ->
        var items_list = mutableListOf<Int>()
        var item_name = values[0].toString() // Search trough and if it matches in examine / name add to array and spawn all to bank | $ For spaces

        for (i in 0 until world.definitions.getCount(ItemDef::class.java)) {
            val def = world.definitions.get(ItemDef::class.java, Item(i).toUnnoted(world.definitions).id)
            val items_name = def.name?.toLowerCase()
            val items_examine = def.examine?.toLowerCase()
            if (!def.isPlaceholder && items_name != "null") {

                if (def.equipSlot.equals(0)) {
                    items_list.add(def.id)
                }

                }
        }
            for (i in 0 until items_list.count()) {
                player.bank.add(items_list[i], 10)
            }
        player.message("Total Count: ${items_list.count()} with keyword: $item_name in their name and examine")
    }
}

on_command("item", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::item 4151 1</col> or <col=801700>::item 4151</col>") { values ->
        val item = values[0].toInt()
        val amount = if (values.size > 1) Math.min(Int.MAX_VALUE.toLong(), values[1].parseAmount()).toInt() else 1
        if (item < world.definitions.getCount(ItemDef::class.java)) {
            val def = world.definitions.get(ItemDef::class.java, Item(item).toUnnoted(world.definitions).id)
            val result = player.inventory.add(item = item, amount = amount, assureFullInsertion = false)
            player.message("You have spawned <col=801700>${DecimalFormat().format(result.completed)} x ${def.name}</col></col> ($item).")
        } else {
            player.message("Item $item does not exist in cache.")
        }
    }
}

on_command("food", Privilege.ADMIN_POWER) {
    player.inventory.add(item = Items.MANTA_RAY, amount = player.inventory.freeSlotCount)
}

on_command("varp", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::varp 173 1</col>") { values ->
        val varp = values[0].toInt()
        val state = values[1].toInt()
        val oldState = player.getVarp(varp)
        player.setVarp(varp, state)
        player.message("Set varp (<col=801700>$varp</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarp(varp)}</col>")
    }
}

on_command("varbit", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::varbit 5451 1</col>") { values ->
        val varbit = values[0].toInt()
        val state = values[1].toInt()
        val oldState = player.getVarbit(varbit)
        player.setVarbit(varbit, state)
        player.message("Set varbit (<col=801700>$varbit</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarbit(varbit)}</col>")
    }
}

on_command("getvarbit", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbit 5451</col>") { values ->
        val varbit = values[0].toInt()
        val state = player.getVarbit(varbit)
        player.message("Get varbit (<col=801700>$varbit</col>): <col=801700>$state</col>")
    }
}

on_command("getvarbits", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbits 83</col>") { values ->
        val varp = values[0].toInt()
        val varbits = mutableListOf<VarbitDef>()
        val totalVarbits = world.definitions.getCount(VarbitDef::class.java)
        for (i in 0 until totalVarbits) {
            val varbit = world.definitions.getNullable(VarbitDef::class.java, i)
            if (varbit?.varp == varp) {
                varbits.add(varbit)
            }
        }
        player.message("Varbits for varp <col=801700>$varp</col>:")
        varbits.forEach { varbit ->
            player.message("  ${varbit.id} [bits ${varbit.startBit}-${varbit.endBit}] [current ${player.getVarbit(varbit.id)}]")
        }
    }
}

on_command("clip", Privilege.ADMIN_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val matrix = chunk.getMatrix(player.tile.height)
    val lx = player.tile.x % 8
    val lz = player.tile.z % 8
    player.message("Tile flags: ${chunk.getMatrix(player.tile.height).get(lx, lz)}")
    Direction.RS_ORDER.forEach { dir ->
        val walkBlocked = matrix.isBlocked(lx, lz, dir, projectile = false)
        val projectileBlocked = matrix.isBlocked(lx, lz, dir, projectile = true)
        val walkable = if (walkBlocked) "<col=801700>blocked</col>" else "<col=178000>walkable</col>"
        val projectile = if (projectileBlocked) "<col=801700>projectiles blocked" else "<col=178000>projectiles allowed"
        player.message("$dir: $walkable - $projectile")
    }
}

on_command("script", Privilege.DEV_POWER){
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::script id args...</col>") { values ->
        val id = values[0].toInt()
        val clientArgs = MutableList<Any>(values.size-1) {}
        for(arg in 1 until values.size)
            clientArgs[arg-1] = values[arg].toIntOrNull() ?: values[arg]
        player.runClientScript(id, *clientArgs.toTypedArray())
        player.message("Executing <col=0000FF>cs_$id</col><col=801700>$clientArgs</col>")
    }
}

on_command("interface", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::interface 214</col>") { values ->
        val component = values[0].toInt()
        player.openInterface(component, InterfaceDestination.MAIN_SCREEN)
        player.message("Opening interface <col=801700>$component</col>")
    }
}

on_command("openinterface", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openinterface interfId parentId pChildId clickThrough isModal</col>") { values ->
        val component = values[0].toInt()
        val parent = values[1].toIntOrNull() ?: getDisplayComponentId(player.interfaces.displayMode)
        val child = values[2].toInt()
        var clickable = values[3].toIntOrNull() ?: 0
        clickable = if(clickable != 1) 0 else 1
        val modal = values[4].toIntOrNull() ?: 1 == 1
        player.openInterface(parent, child, component, clickable, isModal = modal)
        player.message("Opening interface <col=801700>$component</col> on <col=0000ff>$parent:$child</col>")
    }
}

on_command("closeinterface", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::closeinterface interfaceId</col>") { values ->
        val interfaceId = values[0].toInt()
        player.closeInterface(interfaceId)
        player.message("Closing interface <col=801700>$interfaceId</col>")
    }
}

on_command("hitme", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::hitme hitType amount</col>") { values ->
        val hitType = HitType.get(values[0].toInt())
        if(hitType?.name ?: "INVALID" == "INVALID"){
            throw IllegalArgumentException()
        }
        val damage = if(values.size==2 && values[1].matches(Regex("-?\\d+"))) values[1].toInt() else 0
        player.message("${hitType!!.name} hit for $damage")
        player.hit(damage, hitType)
    }
}

on_command("openurl", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openurl google.com</col>") { values ->
        val url = values[0]
        if(!url.startsWith("http://") || !url.startsWith("https://"))
            player.openUrl("https://$url") // not perfect by any means, but simple enough as fallback for easier command
        else
            player.openUrl(url)
    }
}

on_command("transmog", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::transmog transmogId</col>") { values ->
        val id = values[0].toInt()
        player.setTransmogId(id)
        player.message("It's morphing time!")
    }
}

on_command("inv", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::inv invKey itemIds ...</col>") { values ->
        val key = values[0].toInt()
        val items = mutableListOf<Item>()
        for(item in 1 until values.size)
            items.add(Item(values[item].toIntOrNull() ?: 0))
        player.write(UpdateInvFullMessage(containerKey = key, items = items.toTypedArray()))
        player.message("Added $items to inventory($key)")
    }
}

on_command("autoclean", Privilege.ADMIN_POWER){
    Herbs.AUTO_CLEAN = !Herbs.AUTO_CLEAN
    player.message("Herb auto-cleaning ${if(Herbs.AUTO_CLEAN) "enabled" else "disabled"}.")
}

object Command {
    fun tryWithUsage(player: Player, args: Array<String>, failMessage: String, tryUnit: Function1<Array<String>, Unit>) {
        try {
            tryUnit.invoke(args)
        } catch (e: Exception) {
            player.message(failMessage)
            e.printStackTrace()
        }
    }
}
